package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class Ability {

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
