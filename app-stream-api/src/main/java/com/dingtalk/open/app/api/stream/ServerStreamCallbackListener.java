package com.dingtalk.open.app.api.stream;

import java.io.Serializable;

/**
 * @author feiyin
 * @date 2023/9/25
 */
public interface ServerStreamCallbackListener<Request, Response> extends Serializable {

    void execute(Request request, OpenDingTalkStreamObserver<Response> observer);

}