package com.dingtalk.open.app.stream.network.api;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author feiyin
 * @date 2023/11/21
 */
public class NetProxy {
    private final String host;
    private final Integer port;
    private String username;
    private String password;

    public NetProxy(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public NetProxy(String host, Integer port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Proxy getProxy() {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
    }
}
