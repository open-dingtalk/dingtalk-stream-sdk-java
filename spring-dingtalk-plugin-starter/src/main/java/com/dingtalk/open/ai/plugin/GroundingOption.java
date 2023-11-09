package com.dingtalk.open.ai.plugin;

import com.dingtalk.open.app.api.graph.GraphAPIMethod;

/**
 * @author feiyin
 * @date 2023/11/9
 */
public class GroundingOption {

    private String url;

    private GraphAPIMethod method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GraphAPIMethod getMethod() {
        return method;
    }

    public void setMethod(GraphAPIMethod method) {
        this.method = method;
    }
}
