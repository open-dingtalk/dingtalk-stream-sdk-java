package com.dingtalk.open.app.api.message;

import com.alibaba.fastjson.JSONObject;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class GenericOpenDingTalkEvent {

    private String eventId;

    private Long eventBornTime;

    private String eventCorpId;

    private String eventType;

    private String eventUnifiedAppId;

    private JSONObject data;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getEventBornTime() {
        return eventBornTime;
    }

    public void setEventBornTime(Long eventBornTime) {
        this.eventBornTime = eventBornTime;
    }

    public String getEventCorpId() {
        return eventCorpId;
    }

    public void setEventCorpId(String eventCorpId) {
        this.eventCorpId = eventCorpId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventUnifiedAppId() {
        return eventUnifiedAppId;
    }

    public void setEventUnifiedAppId(String eventUnifiedAppId) {
        this.eventUnifiedAppId = eventUnifiedAppId;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
