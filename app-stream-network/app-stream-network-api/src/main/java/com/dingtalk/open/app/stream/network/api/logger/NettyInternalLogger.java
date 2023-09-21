package com.dingtalk.open.app.stream.network.api.logger;

/**
 * @author feiyin
 * @date 2023/2/28
 */
public class NettyInternalLogger implements InternalLogger {
    private io.netty.util.internal.logging.InternalLogger delegate;

    public NettyInternalLogger(io.netty.util.internal.logging.InternalLogger delegate) {
        this.delegate = delegate;
    }

    @Override
    public void info(String format, Object... args) {
        this.delegate.info(format, args);
    }

    @Override
    public void warn(String format, Object... args) {
        this.delegate.error(format, args);
    }

    @Override
    public void error(String format, Exception e, Object... args) {
        this.delegate.error(format, e, args);
    }

    @Override
    public void error(String format, Object... args) {
        this.delegate.error(format, args);
    }
}
