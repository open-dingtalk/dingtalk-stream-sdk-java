package com.dingtalk.open.ai.plugin.schema;

import lombok.Data;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/11/10
 */
@Data
public class Example {

    private String input;

    private Output output;

    @Data
    public static class Output {

        private String abilityKey;

        private Map<String, Object> params;
    }
}
