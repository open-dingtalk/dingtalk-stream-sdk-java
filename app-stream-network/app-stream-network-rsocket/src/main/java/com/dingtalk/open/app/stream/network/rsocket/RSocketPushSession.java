package com.dingtalk.open.app.stream.network.rsocket;

import com.dingtalk.open.app.stream.network.api.BaseSession;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import io.rsocket.RSocket;

/**
 * @author feiyin
 * @date 2023/1/13
 */
public class RSocketPushSession extends BaseSession {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(RSocketPushSession.class);

    private String connectionId;
    private final RSocket receiver;
    private final RSocket sender;
    private final Long deadLine;

    public RSocketPushSession(String connectionId, RSocket receiver, RSocket sender, long ttl) {
        this.connectionId = connectionId;
        this.receiver = receiver;
        this.sender = sender;
        this.deadLine = System.currentTimeMillis() + ttl;
        this.sender.onClose().doOnError(throwable -> LOGGER.error("[DingTalk] establish connection failed, reason={}", throwable.getMessage()));
    }

    @Override
    public boolean isActive() {
        return !sender.isDisposed();
    }

    @Override
    public void close() {
        sender.dispose();
    }

    @Override
    public String getId() {
        return this.connectionId;
    }

    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() > deadLine;
    }
}
