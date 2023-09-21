package com.dingtalk.open.app.api.callback;

import com.dingtalk.open.app.stream.protocol.callback.CallbackResponsePayload;

/**
 * @author feiyin
 * @date 2023/3/17
 */
class SimpleMethod implements CallbackMethod {

    private final OpenDingTalkCallbackListener callback;

    @SuppressWarnings("unchecked")
    public SimpleMethod(OpenDingTalkCallbackListener callback) {
        this.callback = callback;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CallbackResponsePayload execute(Object req) {
        CallbackResponsePayload payload = new CallbackResponsePayload();
        payload.setResponse(callback.execute(req));
        return payload;
    }
}
