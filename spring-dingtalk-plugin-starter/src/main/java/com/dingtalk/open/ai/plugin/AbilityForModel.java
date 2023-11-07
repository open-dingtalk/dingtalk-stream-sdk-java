package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class AbilityForModel {

    @JSONField(name = "input_param")
    private String inputSchema;

    @JSONField(name = "output_param")
    private String outputSchema;

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
