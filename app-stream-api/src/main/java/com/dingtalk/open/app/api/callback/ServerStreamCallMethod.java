package com.dingtalk.open.app.api.callback;

import com.dingtalk.open.app.api.stream.OpenDingTalkStreamObserver;
import com.dingtalk.open.app.api.stream.ServerStreamCallbackListener;
import com.dingtalk.open.app.stream.protocol.callback.CallbackResponsePayload;

/**
 * @author feiyin
 * @date 2023/9/25
 */
public class ServerStreamCallMethod extends CallbackMethodAdaptor {

    private final ServerStreamCallbackListener listener;

    public ServerStreamCallMethod(ServerStreamCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void serverStreamCall(Object request, OpenDingTalkStreamObserver<CallbackResponsePayload> observer) {
        this.listener.execute(request, new OpenDingTalkStreamObserver<Object>() {
            @Override
            public void onNext(Object value) {
                CallbackResponsePayload payload = new CallbackResponsePayload();
                payload.setResponse(value);
                observer.onNext(payload);
            }

            @Override
            public void onError(Throwable t) {
                observer.onError(t);
            }

            @Override
            public void onCompleted() {
                observer.onCompleted();
            }
        });
    }
}
