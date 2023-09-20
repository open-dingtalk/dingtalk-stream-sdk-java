package com.dingtalk.open.app.api;

import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;

/**
 * @author feiyin
 * @date 2022/12/23
 */
public interface GenericEventListener {


    GenericEventListener DEFAULT = event -> EventAckStatus.SUCCESS;

    /**
     * 收到事件
     *
     * @param event
     * @return
     */
    EventAckStatus onEvent(final GenericOpenDingTalkEvent event);


}
