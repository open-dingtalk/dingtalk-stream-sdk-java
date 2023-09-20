package com.dingtalk.open.app.stream.protocol.system.ping;

import com.dingtalk.open.app.stream.protocol.event.ResponsePayload;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class PingPayload implements ResponsePayload {

    private String opaque;

    public String getOpaque() {
        return opaque;
    }

    public void setOpaque(String opaque) {
        this.opaque = opaque;
    }
}
