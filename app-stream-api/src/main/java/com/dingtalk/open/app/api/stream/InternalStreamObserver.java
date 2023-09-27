package com.dingtalk.open.app.api.stream;

import com.dingtalk.open.app.api.common.concurrent.DirectExecutor;
import com.dingtalk.open.app.api.common.concurrent.SerializingExecutor;
import com.dingtalk.open.app.stream.network.api.Context;

import java.util.concurrent.Executor;

/**
 * @author feiyin
 * @date 2023/9/26
 */
public class InternalStreamObserver<Response> implements OpenDingTalkStreamObserver<Response> {
    private final Context context;
    private final Executor executor;

    public InternalStreamObserver(Context context) {
        this.context = context;
        this.executor = new SerializingExecutor(context.getBizExecutor() == null ? DirectExecutor.INSTANCE : context.getBizExecutor());
    }

    @Override
    public void onNext(Response response) {
        executor.execute(() -> context.streamReply(response, false));
    }

    @Override
    public void onError(Throwable t) {
        executor.execute(() -> context.exception(t));
    }

    @Override
    public void onCompleted() {
        executor.execute(() -> context.streamReply(null, true));
    }
}
