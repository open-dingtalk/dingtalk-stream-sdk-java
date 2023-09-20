package com.dingtalk.open.app.api.command;

import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.protocol.CommandExecutor;
import com.dingtalk.open.app.api.protocol.EventCommandExecutor;
import com.dingtalk.open.app.api.protocol.SystemCommandExecutor;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.protocol.CommandType;

import java.util.Collections;
import java.util.Map;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class CommandDispatcher {

    private final Map<CommandType, CommandExecutor> registry;

    public CommandDispatcher(Map<CommandType, CommandExecutor> registry) {
        registry.putIfAbsent(CommandType.SYSTEM, new SystemCommandExecutor());
        this.registry = Collections.unmodifiableMap(registry);
    }

    /**
     * 执行命令
     *
     * @param context
     */
    public void execute(Context context) {
        CommandExecutor executor = registry.get(context.getRequest().getType());
        if (executor == null) {
            context.exception(DingTalkAppError.TYPE_NOT_EXIST.toException());
        } else {
            executor.execute(context);
        }
    }
}
