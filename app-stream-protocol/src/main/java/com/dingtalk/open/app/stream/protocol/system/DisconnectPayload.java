package com.dingtalk.open.app.stream.protocol.system;

/**
 * @author feiyin
 * @date 2023/3/31
 */
public class DisconnectPayload {
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
