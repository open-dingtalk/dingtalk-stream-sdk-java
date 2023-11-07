package com.dingtalk.open.app.api.graph;

/**
 * @author feiyin
 * @date 2023/11/6
 */
public class GraphDescriptor {
    /**
     * 版本号
     */
    private String version;
    /**
     * 方法名
     */
    private String method;
    /**
     * 资源路径
     */
    private String resource;
    /**
     * 应用clientId
     */
    private String clientId;
    /**
     * 输入schema
     */
    private String inputSchema;
    /**
     * 输出schema
     */
    private String outputSchema;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(String inputSchema) {
        this.inputSchema = inputSchema;
    }

    public String getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(String outputSchema) {
        this.outputSchema = outputSchema;
    }
}
