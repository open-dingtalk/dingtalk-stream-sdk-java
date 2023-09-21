package com.dingtalk.open.app.api.protocol;

/**
 * 开放平台业务类型
 *
 * @author feiyin
 * @date 2022/12/28
 */
public enum OpenBusinessType {

    /**
     * 心跳
     */
    PING,

    /**
     * 钉钉事件
     */
    EVENT,
    /**
     * http回调
     */
    CALLBACK;

    public static OpenBusinessType of(String type) {
        for (OpenBusinessType value : OpenBusinessType.values()) {
            if (value.name().equalsIgnoreCase(type)) {
                return value;
            }
        }
        return null;
    }

}
