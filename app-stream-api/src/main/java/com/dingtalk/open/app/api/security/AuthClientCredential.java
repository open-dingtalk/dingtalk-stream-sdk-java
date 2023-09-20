package com.dingtalk.open.app.api.security;

import com.dingtalk.open.app.api.Preconditions;

/**
 * @author feiyin
 * @date 2023/4/10
 */
public class AuthClientCredential extends AbstractDingTalkCredential{
    private String clientId;

    private String clientSecret;

    public AuthClientCredential(String clientId, String clientSecret) {
        this.clientId = Preconditions.notNull(clientId);
        this.clientSecret = Preconditions.notNull(clientSecret);
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }
}
