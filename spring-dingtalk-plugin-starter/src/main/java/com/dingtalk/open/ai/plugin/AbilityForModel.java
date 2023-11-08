package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class AbilityForModel {


    private String description;

    @JSONField(name = "input_param")
    private Object inputSchema;

    @JSONField(name = "output_param")
    private Object outputSchema;

    public Object getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Object inputSchema) {
        this.inputSchema = inputSchema;
    }

    public Object getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Object outputSchema) {
        this.outputSchema = outputSchema;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
