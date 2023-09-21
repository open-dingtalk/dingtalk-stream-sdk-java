package com.dingtalk.open.app.api.open;

/**
 * @author feiyin
 * @date 2023/2/9
 */
public class OpenConnectionResponse {

    private String endpoint;

    private String ticket;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
