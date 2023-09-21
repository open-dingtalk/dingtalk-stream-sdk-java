package com.dingtalk.open.app.api;

/**
 * @author feiyin
 * @date 2023/3/1
 */
class ClientOption {
    /**
     * 最大连接数
     */
    private int maxConnectionCount;
    /**
     * 连接生命周期 ms
     */
    private int connectionTTL;
    /**
     * 建连超时时间
     */
    private long connectTimeout;

    private String openApiHost;

    private KeepAliveOption keepAliveOption;

    public int getMaxConnectionCount() {
        return maxConnectionCount;
    }

    public void setMaxConnectionCount(int maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }

    public int getConnectionTTL() {
        return connectionTTL;
    }

    public void setConnectionTTL(int connectionTTL) {
        this.connectionTTL = connectionTTL;
    }

    public String getOpenApiHost() {
        return openApiHost;
    }

    public void setOpenApiHost(String openApiHost) {
        this.openApiHost = openApiHost;
    }

    public KeepAliveOption getKeepAliveOption() {
        return keepAliveOption;
    }

    public void setKeepAliveOption(KeepAliveOption keepAliveOption) {
        this.keepAliveOption = keepAliveOption;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
