package com.dingtalk.open.ai.plugin;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@ConfigurationProperties(prefix = "spring.dingtalk.plugin")
public class AIProperties {

    private String clientId;

    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
