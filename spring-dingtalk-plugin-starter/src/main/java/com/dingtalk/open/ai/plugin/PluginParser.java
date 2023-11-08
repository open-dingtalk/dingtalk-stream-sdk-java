package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONObject;
import com.dingtalk.open.ai.plugin.annotation.AIAbility;
import com.dingtalk.open.ai.plugin.annotation.AIPlugin;
import com.dingtalk.open.ai.plugin.annotation.FieldDesc;
import com.dingtalk.open.app.stream.protocol.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class PluginParser {

    public static String parseManifest(Class pluginClass) {
        if (!pluginClass.isAnnotationPresent(AIPlugin.class)) {
            throw new RuntimeException("illegal ai plugin service");
        }
        Manifest manifest = new Manifest();
        manifest.setAbilities(new HashMap<>());
        AIPlugin aiPlugin = (AIPlugin) pluginClass.getAnnotation(AIPlugin.class);
        manifest.setSchemaVersion("1.0");
        manifest.setNameForModel(aiPlugin.name());
        manifest.setNameForHuman(aiPlugin.name());
        manifest.setDescriptionForHuman(aiPlugin.description());
        manifest.setDescriptionForModel(aiPlugin.description());
        for (Method method : pluginClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(AIAbility.class)) {
                continue;
            }
            AIAbility aiAbility = method.getAnnotation(AIAbility.class);
            if (!method.isAnnotationPresent(Graph.class)) {
                throw new RuntimeException("graph annotation is not present");
            }

            Graph graph = method.getAnnotation(Graph.class);
            Ability ability = new Ability();
            AbilityForModel abilityForModel = new AbilityForModel();
            AbilityForRuntime abilityForRuntime = new AbilityForRuntime();
            abilityForModel.setDescription(aiAbility.description());
            abilityForModel.setOutputSchema(pojoToJsonSchema(method.getReturnType()));
            abilityForModel.setInputSchema(pojoToJsonSchema(method.getParameterTypes()[0]));

            abilityForRuntime.setType("graph");
            abilityForRuntime.setUrl("https://api.graph.com" + "/v" + graph.version() + graph.resource());
            abilityForRuntime.setMethod(graph.method().name());

            ability.setName(aiAbility.name());
            ability.setAbilityForModel(abilityForModel);
            ability.setAbilityForRuntime(abilityForRuntime);
            manifest.getAbilities().put(UUID.randomUUID().toString(), ability);
        }
        return JSON.toJSONString(manifest, SerializerFeature.DisableCircularReferenceDetect);
    }


    private static Object pojoToJsonSchema(Class pojoClass) {
        if (pojoClass == null) {
            throw new RuntimeException();
        }

        Map<String, Parameter> schema = new HashMap<>();
        int index = 0;
        for (Field declaredField : pojoClass.getDeclaredFields()) {
            Parameter desc = new Parameter();
            FieldDesc fieldDesc = declaredField.getAnnotation(FieldDesc.class);
            if (fieldDesc == null) {
                continue;
            }
            String name = StringUtils.isEmpty(fieldDesc.name()) ? declaredField.getName() : fieldDesc.name();
            String type = parse(declaredField.getType());
            Boolean required = fieldDesc.required();
            String example = fieldDesc.example();
            desc.setType(type);
            desc.setRequired(required);
            desc.setExample(example);
            desc.setDescription(fieldDesc.desc());
            desc.setIndex(index++);
            if (fieldDesc.grounding() != null && fieldDesc.grounding() != GroundingTag.NONE) {
                JSONObject grounding = new JSONObject();
                grounding.put("url", fieldDesc.grounding());
                desc.setGrounding(grounding);
            }
            schema.put(name, desc);
        }
        return schema;
    }


    private static String parse(Class fieldKlass) {
        String type = "object";
        if (fieldKlass == Boolean.class || fieldKlass == boolean.class) {
            type = "boolean";
        } else if (fieldKlass == Short.class || fieldKlass == short.class || fieldKlass == Long.class || fieldKlass == long.class || fieldKlass == Integer.class || fieldKlass == int.class) {
            type = "integer";
        } else if (fieldKlass.isAssignableFrom(Number.class)) {
            type = "number";
        } else if (fieldKlass == String.class) {
            type = "string";
        } else if (fieldKlass.isArray()) {
            type = "array";
        }
        return type;
    }
}
