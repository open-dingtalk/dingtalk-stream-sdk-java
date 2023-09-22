package com.dingtalk.open.app.api.graph;

import com.alibaba.fastjson.annotation.JSONField;

import java.net.URI;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class RequestLine {
    /**
     * 请求方法
     */
    private GraphAPIMethod method;
    /**
     * 请求uri
     */
    private URI uri;
    public GraphAPIMethod getMethod() {
        return method;
    }
    public void setMethod(GraphAPIMethod method) {
        this.method = method;
    }
    public URI getUri() {
        return uri;
    }
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * 获取请求路径
     * @return
     */
    @JSONField(serialize = false)
    public String getPath() {
        return uri == null ? "" : uri.getPath();
    }

    /**
     * 获取请求参数
     * @return
     */
    @JSONField(serialize = false)
    public String getQuery() {
        return uri == null ? "" : uri.getQuery();
    }
}
