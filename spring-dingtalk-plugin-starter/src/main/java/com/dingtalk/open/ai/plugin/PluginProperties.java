package com.dingtalk.open.ai.plugin;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@ConfigurationProperties(prefix = "spring.dingtalk.plugin")
public class PluginProperties {

    private Boolean enabled;

    private String clientId;

    private String clientSecret;

    private String env;

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
