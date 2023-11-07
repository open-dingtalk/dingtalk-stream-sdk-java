package com.dingtalk.open.ai.plugin;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class AbilityForRuntime {
    private String type;
    private String url;
    private String method;
    private String invokeType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getInvokeType() {
        return invokeType;
    }

    public void setInvokeType(String invokeType) {
        this.invokeType = invokeType;
    }
}
