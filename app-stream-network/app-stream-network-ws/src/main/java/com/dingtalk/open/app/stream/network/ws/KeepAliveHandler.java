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
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author feiyin
 * @date 2023/3/29
 */
public class KeepAliveHandler extends SimpleChannelInboundHandler<PongWebSocketFrame> {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(KeepAliveHandler.class);
    private static final HashedWheelTimer TIMER;

    static {
        TIMER = new HashedWheelTimer();
    }

    private final Duration timeout;
    private Channel channel;
    private final Map<String, Timeout> timeouts;
    private final AtomicBoolean active;

    public KeepAliveHandler(Duration timeout) {
        this.timeout = timeout;
        this.active = new AtomicBoolean(false);
        this.timeouts = new ConcurrentHashMap<>();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            if (active.compareAndSet(false, true)) {
                channel = ctx.channel();
            }
        }

        if (evt instanceof IdleStateEvent) {
            channel.eventLoop().execute(new PingTask());
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PongWebSocketFrame msg) throws Exception {
        byte[] data = NettyByteBufUtils.getBytes(msg.content());
        Timeout out = timeouts.remove(new String(data));
        if (out != null) {
            out.cancel();
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        shutdown();
        super.channelInactive(ctx);
    }


    private void shutdown() {
        Iterator<Entry<String, Timeout>> it = timeouts.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Timeout> entry = it.next();
            entry.getValue().cancel();
            it.remove();
        }
    }

    private class PingTask implements Runnable {
        @Override
        public void run() {
            if (!timeouts.isEmpty()) {
                return;
            }
            final String seq = UUID.randomUUID().toString();
            ByteBuf byteBuf = Unpooled.copiedBuffer(seq.getBytes());
            PingWebSocketFrame frame = new PingWebSocketFrame(byteBuf);
            channel.writeAndFlush(frame).addListener(future -> {
                if (future.isSuccess()) {
                    Timeout pingTimeout = TIMER.newTimeout(timeout -> {
                        LOGGER.warn("[DingTalk] connection ping timeout, channel is closing");
                        timeouts.remove(seq);
                        channel.close();
                    }, timeout.toMillis(), TimeUnit.MILLISECONDS);
                    timeouts.put(seq, pingTimeout);
                } else {
                    channel.close();
                }
            });
        }
    }
}
