package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson2.annotation.JSONField;
import com.dingtalk.open.ai.plugin.annotation.FieldDesc;
import com.dingtalk.open.ai.plugin.annotation.GraphGrounding;
import com.dingtalk.open.app.api.graph.GraphAPIMethod;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class Ability {

    @FieldDesc(
            graphGrounding = @GraphGrounding(method = GraphAPIMethod.GET, path = "/url"),
            name = "name",
            example = "",
            desc = "")
    private String name;

    @JSONField(name = "ability_for_model")
    private AbilityForModel abilityForModel;


    @JSONField(name = "ability_for_runtime")
    private AbilityForRuntime abilityForRuntime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AbilityForModel getAbilityForModel() {
        return abilityForModel;
    }

    public void setAbilityForModel(AbilityForModel abilityForModel) {
        this.abilityForModel = abilityForModel;
    }

    public AbilityForRuntime getAbilityForRuntime() {
        return abilityForRuntime;
    }

    public void setAbilityForRuntime(AbilityForRuntime abilityForRuntime) {
        this.abilityForRuntime = abilityForRuntime;
    }
}
