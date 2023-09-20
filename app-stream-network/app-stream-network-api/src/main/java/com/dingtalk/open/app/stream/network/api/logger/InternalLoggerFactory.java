package com.dingtalk.open.app.stream.network.api.logger;

/**
 * @author feiyin
 * @date 2022/12/28
 */
public class InternalLoggerFactory {

    public static InternalLogger getLogger(Class<?> clazz) {
        return new NettyInternalLogger(io.netty.util.internal.logging.InternalLoggerFactory.getInstance(clazz));
    }
}
