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

    private String name;

    private String version;

    private String description;

    private String manifest;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }
}
