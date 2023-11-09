package com.dingtalk.open.ai.plugin.schema;

import com.alibaba.fastjson2.annotation.JSONField;
import com.dingtalk.open.ai.plugin.GroundingOperation;
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

    @JSONField(name = "x-grounding")
    private GroundingOperation grounding;

    private Map<String, Schema> properties;

}
