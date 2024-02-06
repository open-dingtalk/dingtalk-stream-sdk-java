package com.dingtalk.open.app.api.graph;


import com.alibaba.fastjson2.annotation.JSONField;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/9/22
 */
class GraphAPIMessage {
    private Map<String, String> headers;
    private String body;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JSONField(serialize = false)
    public String getHeader(String name) {
        if (name == null || headers == null || headers.isEmpty()) {
            return null;
        }
        return headers.get(name);
    }
}
