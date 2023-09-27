package com.dingtalk.open.app.stream.network.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dingtalk.open.app.stream.network.api.AbstractContext;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.network.api.ServiceException;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.ProtocolRequestFacade;
import com.dingtalk.open.app.stream.protocol.ProtocolResponse;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author feiyin
 * @date 2023/3/27
 */
public class WebSocketContext extends AbstractContext {
    private static InternalLogger LOGGER = InternalLoggerFactory.getLogger(WebSocketContext.class);
    private final ChannelHandlerContext context;
    private final ProtocolRequestFacade request;
    private final String connectionId;
    private final AtomicBoolean completed;

    public WebSocketContext(String connectionId, ProtocolRequestFacade request, ChannelHandlerContext context) {
        this.context = context;
        this.connectionId = connectionId;
        this.request = request;
        this.completed = new AtomicBoolean(false);
    }

    @Override
    public void streamReply(Object payload, boolean endOfStream) {
        if (completed.get()) {
            return;
        }
        ProtocolResponse response = ProtocolResponse.new200Response(request, endOfStream);
        response.setData(payload == null ? null : JSON.toJSONString(payload, SerializerFeature.WriteMapNullValue));
        context.writeAndFlush(response).addListener(future -> {
            if (!future.isSuccess()) {
                LOGGER.error("[DingTalk] websocket connection reply response failed, connectionId={}, messageId={}", connectionId, request.getMessageId(), future.cause());
                context.close();
            }
        });
        if (endOfStream) {
            completed.set(true);
        }
    }

    @Override
    public void exception(Throwable t) {
        if (completed.compareAndSet(false, true)) {
            ProtocolResponse response = t instanceof ServiceException ? ProtocolResponse.newErrorResponse(request, ((ServiceException) t).getCode(), t.getMessage(), true) : ProtocolResponse.new500Response(request, t.getMessage(), true);
            context.writeAndFlush(response).addListener(future -> {
                if (!future.isSuccess()) {
                    LOGGER.error("[DingTalk] websocket connection reply response failed, connectionId={}, messageId={}", connectionId, request.getMessageId(), future.cause());
                    context.close();
                }
            });
        }
    }

    @Override
    public String getConnectionId() {
        return this.connectionId;
    }

    @Override
    public ProtocolRequestFacade getRequest() {
        return this.request;
    }
}
