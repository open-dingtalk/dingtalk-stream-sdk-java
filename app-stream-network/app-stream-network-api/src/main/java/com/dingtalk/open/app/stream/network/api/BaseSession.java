package com.dingtalk.open.app.stream.network.api;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author feiyin
 * @date 2023/5/18
 */
public abstract class BaseSession implements Session {
    private final AtomicBoolean goAway = new AtomicBoolean(false);

    @Override
    public void goAway() {
        this.goAway.set(true);
    }

    @Override
    public boolean isGoAway() {
        return this.goAway.get();
    }
}
