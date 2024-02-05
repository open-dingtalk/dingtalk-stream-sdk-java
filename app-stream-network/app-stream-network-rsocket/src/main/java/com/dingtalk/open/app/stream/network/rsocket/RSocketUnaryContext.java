package com.dingtalk.open.app.stream.network.rsocket;

import com.alibaba.fastjson2.JSON;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.network.api.ServiceException;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.ProtocolRequestFacade;
import com.dingtalk.open.app.stream.protocol.ProtocolResponse;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author feiyin
 * @date 2022/12/28
 */
public class RSocketUnaryContext implements Context {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(RSocketUnaryContext.class);
    private final Sinks.One<ProtocolResponse> responder;
    private final ProtocolRequestFacade request;
    private final String connectionId;
    private final AtomicBoolean finished;

    public RSocketUnaryContext(Sinks.One<ProtocolResponse> responder, String connectionId, ProtocolRequestFacade request) {
        this.responder = responder;
        this.connectionId = connectionId;
        this.finished = new AtomicBoolean(false);
        this.request = request;
    }

    @Override
    public void replay(Object payload) {
        if (finished.compareAndSet(false, true)) {
            ProtocolResponse response = ProtocolResponse.new200Response(request);
            response.setData(JSON.toJSONString(payload));
            this.responder.tryEmitValue(response);
        }
    }

    @Override
    public void exception(Throwable t) {
        if (finished.compareAndSet(false, true)) {
            LOGGER.error("[DingTalk] write exception, id={}, type={}", request.getMessageId(), request.getType(), t);
            if (t instanceof ServiceException) {
                this.responder.tryEmitValue(ProtocolResponse.newErrorResponse(request, ((ServiceException) t).getCode(), t.getMessage()));
            } else {
                this.responder.tryEmitValue(ProtocolResponse.new500Response(request));
            }
        }
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
