package com.dingtalk.open.app.stream.protocol.event;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class AckPayload implements ResponsePayload {
    private EventAckStatus status;
    private String message;

    public EventAckStatus getStatus() {
        return status;
    }

    public void setStatus(EventAckStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
