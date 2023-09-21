package com.dingtalk.open.app.stream.protocol.callback;

import com.dingtalk.open.app.stream.protocol.event.ResponsePayload;

/**
 * @author feiyin
 * @date 2023/3/17
 */
public class CallbackResponsePayload implements ResponsePayload {
    private Object response;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
