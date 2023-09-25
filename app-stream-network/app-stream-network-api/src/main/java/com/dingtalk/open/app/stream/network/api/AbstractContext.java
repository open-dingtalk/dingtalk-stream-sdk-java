package com.dingtalk.open.app.stream.network.api;

import java.util.concurrent.Executor;

/**
 * @author feiyin
 * @date 2023/9/25
 */
public abstract class AbstractContext implements Context {

    private Executor bizExecutor;

    public Executor getBizExecutor() {
        return this.bizExecutor;
    }

    public void setBizExecutor(Executor executor) {
        this.bizExecutor = executor;
    }


}
