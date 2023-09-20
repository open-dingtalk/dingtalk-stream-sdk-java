package com.dingtalk.open.app.stream.network.rsocket;


/**
 * @author feiyin
 * @date 2022/12/28
 */
public class SetupPayload {

    private String ticket;

    public SetupPayload(String ticket) {
        this.ticket = ticket;
    }

    public SetupPayload() {

    }

    public void setTicket(String ticket) {
        this.ticket = ticket;

    }
    public String getTicket() {
        return ticket;
    }
}
