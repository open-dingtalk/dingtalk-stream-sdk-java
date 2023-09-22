package com.dingtalk.open.app.api.graph;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class GraphAPIRequest extends GraphAPIMessage {

    private RequestLine requestLine;

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(RequestLine requestLine) {
        this.requestLine = requestLine;
    }
}
