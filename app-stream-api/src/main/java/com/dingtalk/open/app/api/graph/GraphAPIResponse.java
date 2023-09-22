package com.dingtalk.open.app.api.graph;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class GraphAPIResponse extends GraphAPIMessage {

    private StatusLine statusLine;

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
    }
}
