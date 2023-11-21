package com.dingtalk.open.app.stream.network.core;

import com.dingtalk.open.app.stream.network.api.ClientConnectionListener;

import java.net.Proxy;


/**
 * @author feiyin
 * @date 2022/12/28
 */
public class NetWorkService {
    private final DefaultSessionPool sessionPool;

    public NetWorkService(EndPointConnectionFactory factory, ClientConnectionListener listener, int maxConnection, long ttl, long connectTimeout, long keepAliveIdle, Proxy proxy) {
        this.sessionPool = new DefaultSessionPool(factory, maxConnection, ttl, connectTimeout, keepAliveIdle, listener, proxy);
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
