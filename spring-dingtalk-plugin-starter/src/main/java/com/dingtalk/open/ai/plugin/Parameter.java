package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @author feiyin
 * @date 2023/11/8
 */
public class Parameter {

    private String type;

    private String example;

    private Integer index;

    private String description;

    private Boolean required;


    @JSONField(name = "x-grounding")
    private Object grounding;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Object getGrounding() {
        return grounding;
    }

    public void setGrounding(Object grounding) {
        this.grounding = grounding;
    }
}
