package com.dingtalk.open.app.api.common.concurrent;

import java.util.concurrent.Executor;

/**
 * @author feiyin
 * @date 2023/9/25
 */
public class DirectExecutor implements Executor {

    public static final DirectExecutor INSTANCE = new DirectExecutor();

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
