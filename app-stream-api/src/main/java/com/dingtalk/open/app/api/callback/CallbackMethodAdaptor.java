package com.dingtalk.open.app.api.callback;

import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.OpenDingTalkAppException;
import com.dingtalk.open.app.api.stream.OpenDingTalkStreamObserver;
import com.dingtalk.open.app.stream.protocol.callback.CallbackResponsePayload;

/**
 * @author feiyin
 * @date 2023/9/25
 */
public abstract class CallbackMethodAdaptor implements CallbackMethod  {
    @Override
    public CallbackResponsePayload unaryCall(Object req) {
        throw new OpenDingTalkAppException(DingTalkAppError.ILLEGAL_CALLBACK_TYPE);
    }

    @Override
    public void serverStreamCall(Object req, OpenDingTalkStreamObserver<CallbackResponsePayload> observer) {
        throw new OpenDingTalkAppException(DingTalkAppError.ILLEGAL_CALLBACK_TYPE);
    }
}
