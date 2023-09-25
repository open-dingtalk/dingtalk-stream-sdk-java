package com.dingtalk.open.app.api.protocol;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.system.DisconnectPayload;
import com.dingtalk.open.app.stream.protocol.system.SystemTopic;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class SystemCommandExecutor implements CommandExecutor {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(SystemCommandExecutor.class);

    @Override
    public void execute(Context context) {
        if (SystemTopic.PING.equals(context.getRequest().getTopic())) {
            context.streamReply(JSON.parseObject(context.getRequest().getData()), true);
            return;
        }

        if (SystemTopic.DISCONNECT.equals(context.getRequest().getTopic())) {
            DisconnectPayload payload = JSON.parseObject(context.getRequest().getData(), DisconnectPayload.class);
            LOGGER.info("[DingTalk] receive connection close info, connection will close later, connectionId={}, reason={}", context.getConnectionId(), payload.getReason());
        }
    }
}
