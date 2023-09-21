package com.dingtalk.open.app.stream.network.core;

import com.dingtalk.open.app.stream.network.api.ClientConnectionListener;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;

/**
 * @author feiyin
 * @date 2022/12/28
 */
public class NetWorkService {
    private final DefaultSessionPool sessionPool;

    public NetWorkService(EndPointConnectionFactory factory,
                          ClientConnectionListener listener,
                          int maxConnection,
                          long ttl,
                          long connectTimeout,
                          long keepAliveIdle) {
        this.sessionPool = new DefaultSessionPool(factory, maxConnection, ttl, connectTimeout, keepAliveIdle, listener);
    }
    /**
     * 开始
     */
    public void start() {
        sessionPool.start();
    }

    /**
     * 关闭
     */
    public void shutdown() throws Exception {
        sessionPool.shutdown();
    }
}
