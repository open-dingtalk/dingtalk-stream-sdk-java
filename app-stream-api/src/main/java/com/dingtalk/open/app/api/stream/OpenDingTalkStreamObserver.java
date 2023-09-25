package com.dingtalk.open.app.api.stream;

/**
 * @author feiyin
 * @date 2023/9/25
 */
public interface OpenDingTalkStreamObserver<Response> {

    void onNext(Response response);

    void onError(Throwable t);

    void onCompleted();

}

