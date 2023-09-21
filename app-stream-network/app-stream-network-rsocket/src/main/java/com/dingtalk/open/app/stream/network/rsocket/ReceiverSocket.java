package com.dingtalk.open.app.stream.network.rsocket;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.stream.network.api.ClientConnectionListener;
import com.dingtalk.open.app.stream.network.api.exception.DingTalkNetworkException;
import com.dingtalk.open.app.stream.network.api.exception.NetWorkError;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.DefaultProtocolRequestFacade;
import com.dingtalk.open.app.stream.protocol.ProtocolRequest;
import com.dingtalk.open.app.stream.protocol.ProtocolResponse;
import io.netty.buffer.ByteBufUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author feiyin
 * @date 2023/1/13
 */
public class ReceiverSocket implements RSocket {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(ReceiverSocket.class);
    public final ClientConnectionListener listener;
    private final String connectionId;
    private final AtomicBoolean active = new AtomicBoolean(true);

    public ReceiverSocket(String connectionId, ClientConnectionListener listener) {
        this.connectionId = connectionId;
        this.listener = listener;
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        Sinks.One<ProtocolResponse> sink = Sinks.one();
        try {
            ProtocolRequest request = JSON.parseObject(ByteBufUtil.getBytes(payload.data()), ProtocolRequest.class);
            listener.receive(new RSocketUnaryContext(sink, this.connectionId, new DefaultProtocolRequestFacade(request)));
            return sink.asMono().map(object -> {
                if (object == null) {
                    throw new DingTalkNetworkException(NetWorkError.PROTOCOL_ILLEGAL);
                }
                return DefaultPayload.create(JSON.toJSONBytes(object));
            });
        } finally {
            payload.release();
        }
    }

    @Override
    public void dispose() {
        LOGGER.info("[DingTalk] connection is closed passively,connection={}", connectionId);
        if (active.compareAndSet(true, false)) {
            listener.onDisConnection(connectionId);
        }
    }

    @Override
    public boolean isDisposed() {
        return !active.get();
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        return RSocket.super.metadataPush(payload);
    }
}
