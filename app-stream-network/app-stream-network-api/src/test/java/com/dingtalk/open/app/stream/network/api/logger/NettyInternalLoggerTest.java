package com.dingtalk.open.app.stream.network.api.logger;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

public class NettyInternalLoggerTest {

    @Test
    public void warnDelegatesToWarnLevel() {
        AtomicReference<String> invokedMethod = new AtomicReference<>();
        io.netty.util.internal.logging.InternalLogger delegate =
                (io.netty.util.internal.logging.InternalLogger) Proxy.newProxyInstance(
                        getClass().getClassLoader(),
                        new Class<?>[]{io.netty.util.internal.logging.InternalLogger.class},
                        (proxy, method, args) -> {
                            invokedMethod.set(method.getName());
                            return null;
                        });

        NettyInternalLogger logger = new NettyInternalLogger(delegate);
        logger.warn("recoverable network error, connectionId={}", "conn-test");

        Assert.assertEquals("warn", invokedMethod.get());
    }
}
