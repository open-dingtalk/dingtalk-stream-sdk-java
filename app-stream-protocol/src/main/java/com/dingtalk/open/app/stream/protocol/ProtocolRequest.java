package com.dingtalk.open.app.stream.protocol;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class ProtocolRequest {
    /**
     * 版本号
     */
    private String specVersion;
    /**
     * 类型
     */
    private CommandType type;
    /**
     * 头信息
     */
    private Map<String, String> headers;
    /**
     * 数据
     */
    private String data;

    public String getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
