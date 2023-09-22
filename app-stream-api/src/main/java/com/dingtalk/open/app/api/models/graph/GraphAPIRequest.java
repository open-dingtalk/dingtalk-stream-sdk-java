package com.dingtalk.open.app.api.models.graph;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class GraphAPIRequest extends GraphAPIMessage{

    private RequestLine requestLine;

    private String body;

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public void setRequestLine(RequestLine requestLine) {
        this.requestLine = requestLine;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
