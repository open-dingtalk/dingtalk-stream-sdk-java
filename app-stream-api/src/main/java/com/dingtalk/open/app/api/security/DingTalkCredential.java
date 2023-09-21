package com.dingtalk.open.app.api.security;

/**
 * @author feiyin
 * @date 2023/2/9
 */
public interface DingTalkCredential {
    /**
     * 获取aK
     *
     * @return
     */
    String getClientId();

    /**
     * 获取sk
     *
     * @return
     */
    String getClientSecret();


}
