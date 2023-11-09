package com.dingtalk.open.ai.plugin.api;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class ReportPluginRequest {
    /**
     * clientId
     */
    private String clientId;

    private String manifest;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }
}
