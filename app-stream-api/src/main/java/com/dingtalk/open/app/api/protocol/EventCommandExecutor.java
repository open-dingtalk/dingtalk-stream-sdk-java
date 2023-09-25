package com.dingtalk.open.app.api.protocol;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.Preconditions;
import com.dingtalk.open.app.api.message.EventHeaders;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.ContentType;
import com.dingtalk.open.app.stream.protocol.event.AckPayload;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class EventCommandExecutor implements CommandExecutor {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(EventCommandExecutor.class);
    private final GenericEventListener listener;

    public EventCommandExecutor(GenericEventListener listener) {
        this.listener = Preconditions.notNull(listener);
    }

    @Override
    public void execute(Context context) {
        ContentType contentType = ContentType.of(context.getRequest().getContentType());
        if (contentType == null) {
            context.exception(DingTalkAppError.UNKNOWN_CONTENT_TYPE.toException());
            return;
        }
        MessageConverter converter = MessageConverterMapping.getConverter(contentType);
        if (converter == null) {
            context.exception(DingTalkAppError.UNKNOWN_CONTENT_TYPE.toException());
            return;
        }

        final GenericOpenDingTalkEvent message = new GenericOpenDingTalkEvent();
        Object object = converter.convert(context.getRequest().getData(), JSONObject.class);
        if (object instanceof JSONObject) {
            message.setData((JSONObject) object);
        } else {
            context.exception(DingTalkAppError.BAD_REQUEST_TYPE.toException());
            return;
        }

        message.setEventId(context.getRequest().getHeader(EventHeaders.ID));
        if (context.getRequest().getHeader(EventHeaders.TIME) != null) {
            message.setEventBornTime(Long.valueOf(context.getRequest().getHeader(EventHeaders.TIME)));
        }
        message.setEventCorpId(context.getRequest().getHeader(EventHeaders.CORP_ID));
        message.setEventType(context.getRequest().getHeader(EventHeaders.TYPE));
        message.setEventUnifiedAppId(context.getRequest().getHeader(EventHeaders.APP_ID));
        message.setData((JSONObject) object);
        EventAckStatus status;
        try {
            status = listener.onEvent(message);
        } catch (Exception e) {
            LOGGER.error("[DingTalk] consume message failed, eventId={}", message.getEventId(), e);
            context.exception(e);
            return;
        }
        AckPayload ackPayload = new AckPayload();
        ackPayload.setStatus(status);
        context.streamReply(ackPayload, true);
    }
}
