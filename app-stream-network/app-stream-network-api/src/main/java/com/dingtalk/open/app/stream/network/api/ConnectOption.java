package com.dingtalk.open.app.stream.network.api;

import java.time.Duration;

/**
 * @author feiyin
 * @date 2023/4/13
 */
public class ConnectOption {
    private long timeout;
    private long ttl;
    private Duration keepAliveIdle;
    private Duration keepAliveTimeout;

    private ConnectOption(long timeout, long ttl, Duration keepAliveIdle, Duration keepAliveTimeout) {
        this.timeout = timeout;
        this.ttl = ttl;
        this.keepAliveIdle = keepAliveIdle;
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long timeout;
        private long ttl;
        private Duration keepAliveIdle;
        private Duration keepAliveTimeout;

        public Builder setTimeout(long timeout) {
            Builder.this.timeout = timeout;
            return Builder.this;
        }

        public Builder setTtl(long ttl) {
            Builder.this.ttl = ttl;
            return Builder.this;
        }

        public Builder setKeepAliveIdle(Duration keepAliveIdle) {
            this.keepAliveIdle = keepAliveIdle;
            return Builder.this;
        }

        public Builder setKeepAliveTimeout(Duration timeout) {
            this.keepAliveTimeout = timeout;
            return Builder.this;
        }

        public ConnectOption build() {
            return new ConnectOption(this.timeout, this.ttl, this.keepAliveIdle, this.keepAliveTimeout);
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public long getTtl() {
        return ttl;
    }

    public Duration getKeepAliveIdle() {
        return keepAliveIdle;
    }

    public Duration getKeepAliveTimeout() {
        return keepAliveTimeout;
    }
}
