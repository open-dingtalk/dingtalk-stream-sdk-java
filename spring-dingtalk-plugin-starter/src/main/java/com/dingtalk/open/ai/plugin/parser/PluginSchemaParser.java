package com.dingtalk.open.ai.plugin.parser;

import com.dingtalk.open.ai.plugin.Graph;
import com.dingtalk.open.ai.plugin.GroundingOperation;
import com.dingtalk.open.ai.plugin.GroundingTag;
import com.dingtalk.open.ai.plugin.annotation.AIAbility;
import com.dingtalk.open.ai.plugin.annotation.AIPlugin;
import com.dingtalk.open.ai.plugin.annotation.FieldDesc;
import com.dingtalk.open.ai.plugin.schema.*;
import com.dingtalk.open.app.api.graph.GraphAPIMethod;
import com.dingtalk.open.app.stream.protocol.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author feiyin
 * @date 2023/11/9
 */
public class PluginSchemaParser {

    public static OpenApi parseManifest(Class pluginClass) {
        if (!pluginClass.isAnnotationPresent(AIPlugin.class)) {
            throw new RuntimeException("illegal ai plugin service");
        }
        OpenApi openApi = new OpenApi();

        AIPlugin aiPlugin = (AIPlugin) pluginClass.getAnnotation(AIPlugin.class);
        openApi.setOpenapi("3.0.0");
        openApi.setServers(new String[]{"https://graph.dingtalk.com"});
        //解析插件信息
        Info info = new Info();
        info.setVersion(aiPlugin.version());
        info.setDescription(aiPlugin.description());
        info.setTitle(aiPlugin.name());
        openApi.setInfo(info);
        openApi.setPaths(new HashMap<>());
        for (Method method : pluginClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(AIAbility.class)) {
                continue;
            }
            AIAbility aiAbility = method.getAnnotation(AIAbility.class);
            if (!method.isAnnotationPresent(Graph.class)) {
                throw new RuntimeException("graph annotation is not present");
            }
            Graph graph = method.getAnnotation(Graph.class);
            PathItem pathItem = new PathItem();
            Operation operation = new Operation();
            operation.setOperationId(aiAbility.name());
            operation.setSummary(aiAbility.description());
            operation.setExamples(new Example[]{});
            RequestBody requestBody = new RequestBody();
            requestBody.setRequired(true);
            requestBody.setContent(new HashMap<>());
            MediaType mediaType = new MediaType();
            mediaType.setSchema(pojoToJsonSchema(method.getParameterTypes()[0]));
            requestBody.getContent().put(MediaType.APPLICATION_JSON, mediaType);
            operation.setRequestBody(requestBody);
            operation.setResponses(new HashMap<>());
            MediaType responseMediaType = new MediaType();
            responseMediaType.setSchema(pojoToJsonSchema(method.getReturnType()));
            Response response = new Response();
            response.setContent(new HashMap<>());
            response.getContent().put(MediaType.APPLICATION_JSON, responseMediaType);
            operation.getResponses().put(StatusCode.OK, response);
            if (GraphAPIMethod.GET.equals(graph.method())) {
                pathItem.setGet(operation);
            } else {
                pathItem.setPost(operation);
            }
            openApi.getPaths().put("/v" + graph.version() + graph.resource(), pathItem);
        }
        return openApi;
    }


    private static Schema pojoToJsonSchema(Class pojoClass) {
        if (pojoClass == null) {
            throw new RuntimeException();
        }

        Schema schema = new Schema();
        schema.setType("object");
        schema.setProperties(new HashMap<>());
        int index = 0;
        for (Field declaredField : pojoClass.getDeclaredFields()) {
            Schema desc = new Schema();
            FieldDesc fieldDesc = declaredField.getAnnotation(FieldDesc.class);
            if (fieldDesc == null) {
                continue;
            }
            String name = StringUtils.isEmpty(fieldDesc.name()) ? declaredField.getName() : fieldDesc.name();
            String type = parseType(declaredField.getType());
            Boolean required = fieldDesc.required();
            String example = fieldDesc.example();
            desc.setType(type);
            desc.setRequired(required);
            desc.setExample(example);
            desc.setDescription(fieldDesc.desc());
            desc.setIndex(index++);
            //system grounding优先级最高
            if (fieldDesc.systemGrounding() != null && fieldDesc.systemGrounding() != GroundingTag.NONE) {
                GroundingOperation option = new GroundingOperation();
                option.setUrl(fieldDesc.systemGrounding().name());
                desc.setGrounding(option);
            } else if (fieldDesc.graphGrounding() != null && !StringUtils.isEmpty(fieldDesc.graphGrounding().path())) {
                GroundingOperation operation = new GroundingOperation();
                operation.setUrl(fieldDesc.graphGrounding().path());
                operation.setMethod(fieldDesc.graphGrounding().method());
                desc.setGrounding(operation);
            }
            schema.getProperties().put(name, desc);
        }
        return schema;
    }


    private static String parseType(Class fieldKlass) {
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
