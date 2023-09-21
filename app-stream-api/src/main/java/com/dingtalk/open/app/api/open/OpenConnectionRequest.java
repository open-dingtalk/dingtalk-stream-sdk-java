package com.dingtalk.open.app.api.open;


import com.dingtalk.open.app.stream.network.core.Subscription;

import java.util.Set;

/**
 * @author feiyin
 * @date 2023/2/9
 */
public class OpenConnectionRequest {
    /**
     * key
     */
    private String clientId;
    /**
     * secret
     */
    private String clientSecret;

    private String ua;

    private Set<Subscription> subscriptions;

    private String localIp;


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

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }
}

