package com.dingtalk.open.app.api.open;


import java.net.Proxy;

/**
 * @author feiyin
 * @date 2023/3/1
 */
public class OpenApiClientBuilder {

    private OpenApiClientBuilder() {

    }

    public static OpenApiClientBuilder create() {
        return new OpenApiClientBuilder();
    }

    public String host;

    private int timeout = 3000;


    public OpenApiClientBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public OpenApiClientBuilder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public OpenApiClient build() {
        return new HttpOpenApiClient(host, timeout);
    }
}
