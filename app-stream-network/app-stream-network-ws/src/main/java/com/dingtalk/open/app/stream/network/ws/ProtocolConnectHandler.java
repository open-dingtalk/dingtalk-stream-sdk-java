package com.dingtalk.open.app.stream.network.ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author feiyin
 * @date 2023/9/7
 */
public class ProtocolConnectHandler extends ChannelDuplexHandler {
    private static final HashedWheelTimer TIMER = new HashedWheelTimer();

    private Timeout timeout;

    private final CompletableFuture<Channel> future;
    /**
     * 建连超时时间
     */
    private final Long connectTimeout;

    public ProtocolConnectHandler(CompletableFuture<Channel> future, Long connectTimeout) {
        this.connectTimeout = connectTimeout;
        this.future = future;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            if (timeout != null && !timeout.isExpired()) {
                timeout.cancel();
                //执行回调
                future.complete(ctx.channel());
                ctx.pipeline().remove(ProtocolConnectHandler.class);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        this.timeout = TIMER.newTimeout(t -> {
            ProtocolConnectHandler.this.future.completeExceptionally(new TimeoutException("connect timeout"));
        }, connectTimeout, TimeUnit.MILLISECONDS);
        super.connect(ctx, remoteAddress, localAddress, promise);
    }
}
