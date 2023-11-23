package com.dingtalk.open.app.stream.network.api;

/**
 * @author feiyin
 * @date 2023/11/21
 */
public class NetProxy {
    private String host;
    private Integer port;

    public NetProxy(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getIp() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
