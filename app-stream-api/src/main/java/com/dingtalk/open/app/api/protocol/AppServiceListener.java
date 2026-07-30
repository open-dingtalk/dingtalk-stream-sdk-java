package com.dingtalk.open.app.api.protocol;

import com.dingtalk.open.app.api.Preconditions;
import com.dingtalk.open.app.api.command.CommandDispatcher;
import com.dingtalk.open.app.stream.network.api.ClientConnectionListener;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.CommandType;
import com.dingtalk.open.app.stream.protocol.event.AckPayload;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

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
        if (context.getRequest().getType() == CommandType.SYSTEM) {
            dispatch(context);
            return;
        }
        try {
            this.executor.execute(() -> dispatch(context));
        } catch (RejectedExecutionException e) {
            if (context.getRequest().getType() == CommandType.EVENT) {
                AckPayload ackPayload = new AckPayload();
                ackPayload.setStatus(EventAckStatus.LATER);
                ackPayload.setMessage("consumer capacity reached");
                context.replay(ackPayload);
            } else {
                // CALLBACK responses are application-specific. Leaving the
                // frame unacknowledged lets the server retry it later.
                LOGGER.warn("[DingTalk] callback consumer capacity reached, topic={}",
                        context.getRequest().getTopic());
            }
        }
    }

    private void dispatch(Context context) {
        try {
            commandDispatcher.execute(context);
        } catch (Throwable e) {
            LOGGER.error("[DingTalk] dispatch command failed, {}", e);
            context.exception(e);
        }
    }
}
