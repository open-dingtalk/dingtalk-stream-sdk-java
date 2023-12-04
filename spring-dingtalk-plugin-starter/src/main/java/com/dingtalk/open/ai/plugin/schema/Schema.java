package com.dingtalk.open.ai.plugin.schema;

import lombok.Data;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/11/9
 */
@Data
public class Schema {

    private String type;

    private Boolean required;

    private String description;

    private String example;

    private Integer index;

    private Map<String, Schema> properties;

    private Schema items;

}
