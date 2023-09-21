package com.dingtalk.open.app.stream.network.ws;

import com.dingtalk.open.app.stream.network.api.ClientConnectionListener;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.DefaultProtocolRequestFacade;
import com.dingtalk.open.app.stream.protocol.ProtocolRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;

/**
 * @author feiyin
 * @date 2023/3/27
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ProtocolRequest> {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(ProtocolFrameHandler.class);

    private final ClientConnectionListener listener;
    private final String connectionId;

    public NettyClientHandler(String connectionId, ClientConnectionListener listener) {
        this.connectionId = connectionId;
        this.listener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolRequest request) throws Exception {
        listener.receive(new WebSocketContext(connectionId, new DefaultProtocolRequestFacade(request), ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("[DingTalk] connection is closed, connectionId={}", connectionId);
        listener.onDisConnection(connectionId);
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            //wss握手成功后
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof WebSocketClientHandshakeException) {
            LOGGER.error("[DingTalk] connection establish error, please check connectionId={}", connectionId, cause);
        } else {
            LOGGER.error("[DingTalk] connection operation failed, connectionId={}", connectionId, cause);
        }
    }
}
