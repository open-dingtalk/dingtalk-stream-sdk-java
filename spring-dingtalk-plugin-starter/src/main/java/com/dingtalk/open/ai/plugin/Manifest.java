package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class Manifest {

    @JSONField(name = "schema_version")
    private String schemaVersion;

    @JSONField(name = "name_for_model")
    private String nameForModel;

    @JSONField(name = "name_for_human")
    private String nameForHuman;

    @JSONField(name = "description_for_model")
    private String descriptionForModel;

    @JSONField(name = "description_for_human")
    private String descriptionForHuman;

    private Map<String, Ability> abilities;


    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getNameForModel() {
        return nameForModel;
    }

    public void setNameForModel(String nameForModel) {
        this.nameForModel = nameForModel;
    }

    public String getNameForHuman() {
        return nameForHuman;
    }

    public void setNameForHuman(String nameForHuman) {
        this.nameForHuman = nameForHuman;
    }

    public String getDescriptionForModel() {
        return descriptionForModel;
    }

    public void setDescriptionForModel(String descriptionForModel) {
        this.descriptionForModel = descriptionForModel;
    }

    public String getDescriptionForHuman() {
        return descriptionForHuman;
    }

    public void setDescriptionForHuman(String descriptionForHuman) {
        this.descriptionForHuman = descriptionForHuman;
    }

    public Map<String, Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(Map<String, Ability> abilities) {
        this.abilities = abilities;
    }
}
