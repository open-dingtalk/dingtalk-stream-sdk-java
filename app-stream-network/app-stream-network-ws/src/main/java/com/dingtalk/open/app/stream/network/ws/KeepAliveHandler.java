package com.dingtalk.open.app.stream.network.ws;

import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.network.api.utils.NettyByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author feiyin
 * @date 2023/3/29
 */
public class KeepAliveHandler extends SimpleChannelInboundHandler<PongWebSocketFrame> {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(KeepAliveHandler.class);
    private final Duration timeout;
    private volatile Channel channel;
    private final AtomicBoolean active;
    private final AtomicReference<PendingPing> pendingPing;

    public KeepAliveHandler(Duration timeout) {
        this.timeout = timeout;
        this.active = new AtomicBoolean(false);
        this.pendingPing = new AtomicReference<>();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            if (active.compareAndSet(false, true)) {
                channel = ctx.channel();
            }
        }

        if (evt instanceof IdleStateEvent) {
            // IdleStateHandler starts counting after TCP connect, before WS handshake.
            // Never touch the channel field until handshake completes, otherwise NPE:
            // connection operation failed (KeepAliveHandler.java:56)
            if (active.get()) {
                final Channel ch = channel != null ? channel : ctx.channel();
                if (ch != null && ch.isActive()) {
                    ch.eventLoop().execute(new PingTask(ch));
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PongWebSocketFrame msg) throws Exception {
        byte[] data = NettyByteBufUtils.getBytes(msg.content());
        PendingPing ping = pendingPing.get();
        if (ping != null
                && ping.seq.equals(new String(data, StandardCharsets.UTF_8))
                && pendingPing.compareAndSet(ping, null)) {
            ping.cancelTimeout();
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        active.set(false);
        channel = null;
        shutdown();
        super.channelInactive(ctx);
    }


    private void shutdown() {
        PendingPing ping = pendingPing.getAndSet(null);
        if (ping != null) {
            ping.cancelTimeout();
        }
    }

    private static class PendingPing {
        private final String seq;
        private volatile ScheduledFuture<?> timeout;

        private PendingPing(String seq) {
            this.seq = seq;
        }

        private void cancelTimeout() {
            ScheduledFuture<?> current = timeout;
            if (current != null) {
                current.cancel(false);
            }
        }
    }

    private class PingTask implements Runnable {
        private final Channel target;

        private PingTask(Channel target) {
            this.target = target;
        }

        @Override
        public void run() {
            if (!active.get() || target == null || !target.isActive()) {
                return;
            }
            final String seq = UUID.randomUUID().toString();
            final PendingPing ping = new PendingPing(seq);
            if (!pendingPing.compareAndSet(null, ping)) {
                return;
            }

            ScheduledFuture<?> pingTimeout = target.eventLoop().schedule(() -> {
                if (pendingPing.compareAndSet(ping, null)) {
                    active.set(false);
                    LOGGER.warn("[DingTalk] connection ping timeout, channel is closing");
                    target.close();
                }
            }, timeout.toMillis(), TimeUnit.MILLISECONDS);
            ping.timeout = pingTimeout;

            // channelInactive may clear the pending ping while the timeout is being installed.
            if (pendingPing.get() != ping) {
                pingTimeout.cancel(false);
                return;
            }

            ByteBuf byteBuf = Unpooled.copiedBuffer(seq.getBytes(StandardCharsets.UTF_8));
            PingWebSocketFrame frame = new PingWebSocketFrame(byteBuf);
            target.writeAndFlush(frame).addListener(future -> {
                if (!future.isSuccess() && pendingPing.compareAndSet(ping, null)) {
                    active.set(false);
                    pingTimeout.cancel(false);
                    target.close();
                }
            });
        }
    }
}
