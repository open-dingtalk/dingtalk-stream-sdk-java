package com.dingtalk.open.app.stream.network.ws;

import com.dingtalk.open.app.stream.network.api.BaseSession;
import io.netty.channel.Channel;

/**
 * @author feiyin
 * @date 2023/3/27
 */
public class WebSocketSession extends BaseSession {

    private final Channel channel;

    private final String connectionId;

    private final long deadLine;

    public WebSocketSession(Channel channel, String connectionId, long ttl) {
        this.connectionId = connectionId;
        this.channel = channel;
        this.deadLine = System.currentTimeMillis() + ttl;
    }

    @Override
    public boolean isActive() {
        return channel.isActive();
    }

    @Override
    public synchronized void close() {
        if (isActive()) {
            this.channel.close();
        }
    }

    @Override
    public String getId() {
        return this.connectionId;
    }

    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() > this.deadLine;
    }
}
