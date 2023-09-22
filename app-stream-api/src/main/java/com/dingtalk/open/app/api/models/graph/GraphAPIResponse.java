package com.dingtalk.open.app.api.models.graph;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class GraphAPIResponse extends GraphAPIMessage {

    private StatusLine statusLine;

    private String body;

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
