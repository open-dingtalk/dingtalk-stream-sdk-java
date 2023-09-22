package com.dingtalk.open.app.api.graph;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class RequestLine {
    private GraphAPIMethod method;
    private String uri;

    public GraphAPIMethod getMethod() {
        return method;
    }

    public void setMethod(GraphAPIMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
