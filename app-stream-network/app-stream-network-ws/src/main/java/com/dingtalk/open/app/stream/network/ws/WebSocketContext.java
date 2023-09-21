package com.dingtalk.open.app.stream.network.ws;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.network.api.ServiceException;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.ProtocolRequestFacade;
import com.dingtalk.open.app.stream.protocol.ProtocolResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feiyin
 * @date 2023/3/27
 */
public class WebSocketContext implements Context {
    private static InternalLogger LOGGER = InternalLoggerFactory.getLogger(WebSocketContext.class);
    private final ChannelHandlerContext context;
    private final ProtocolRequestFacade request;
    private final String connectionId;

    public WebSocketContext(String connectionId, ProtocolRequestFacade request, ChannelHandlerContext context) {
        this.context = context;
        this.connectionId = connectionId;
        this.request = request;
    }

    @Override
    public void replay(Object payload) {
        ProtocolResponse response = ProtocolResponse.new200Response(request);
        response.setData(JSON.toJSONString(payload));
        context.writeAndFlush(response).addListener(future -> {
            if (!future.isSuccess()) {
                LOGGER.error("[DingTalk] websocket connection reply response failed, connectionId={}, messageId={}", connectionId, request.getMessageId(), future.cause());
                context.close();
            }
        });
    }

    @Override
    public void exception(Throwable t) {
        ProtocolResponse response = t instanceof ServiceException ? ProtocolResponse.newErrorResponse(request, ((ServiceException) t).getCode(), t.getMessage()) : ProtocolResponse.new500Response(request);
        context.writeAndFlush(response).addListener(future -> {
            if (!future.isSuccess()) {
                LOGGER.error("[DingTalk] websocket connection reply response failed, connectionId={}, messageId={}", connectionId, request.getMessageId(), future.cause());
                context.close();
            }
        });
    }

    @Override
    public String connectionId() {
        return this.connectionId;
    }

    @Override
    public ProtocolRequestFacade getRequest() {
        return this.request;
    }
}
