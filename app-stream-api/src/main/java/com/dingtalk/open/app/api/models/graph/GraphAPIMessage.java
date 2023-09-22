package com.dingtalk.open.app.api.models.graph;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/9/22
 */
class GraphAPIMessage {
    private Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

}
