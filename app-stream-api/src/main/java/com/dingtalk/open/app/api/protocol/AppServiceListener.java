package com.dingtalk.open.app.api.protocol;

import com.dingtalk.open.app.api.Preconditions;
import com.dingtalk.open.app.api.command.CommandDispatcher;
import com.dingtalk.open.app.stream.network.api.ClientConnectionListener;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author feiyin
 * @date 2022/12/28
 */
public class AppServiceListener implements ClientConnectionListener {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(AppServiceListener.class);
    private final CommandDispatcher commandDispatcher;
    private final Executor executor;

    public AppServiceListener(CommandDispatcher commandDispatcher, Executor executor) {
        this.commandDispatcher = Preconditions.notNull(commandDispatcher);
        this.executor = Preconditions.notNull(executor);
    }

    @Override
    public void receive(Context context) {
        this.executor.execute(() -> {
            try {
                commandDispatcher.execute(context);
            } catch (Exception e) {
                LOGGER.error("[DingTalk] dispatch command failed, {}", e);
            }
        });
    }
}
