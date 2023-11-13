package com.dingtalk.open.ai.plugin.schema;

import com.dingtalk.open.ai.plugin.FileExampleReader;
import com.dingtalk.open.ai.plugin.GroundingOperation;
import com.dingtalk.open.ai.plugin.GroundingTag;
import com.dingtalk.open.ai.plugin.TextExampleReader;
import com.dingtalk.open.ai.plugin.annotation.*;
import com.dingtalk.open.ai.plugin.error.ChatPluginError;
import com.dingtalk.open.ai.plugin.error.ChatPluginException;
import com.dingtalk.open.ai.plugin.util.Pair;
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

    public static AIPluginSchema parseSchema(Class<?> pluginClass) {
        if (!pluginClass.isAnnotationPresent(AIPlugin.class)) {
            return null;
        }

        AIPluginSchema schema = new AIPluginSchema();

        AIPlugin aiPlugin = pluginClass.getAnnotation(AIPlugin.class);
        schema.setOpenapi(Constants.SCHEMA_VERSION);
        schema.setServers(Constants.SERVER_HOST);
        //解析插件信息
        final Info info = new Info();
        info.setVersion(aiPlugin.version());
        info.setDescription(aiPlugin.description());
        info.setTitle(aiPlugin.name());
        schema.setInfo(info);
        schema.setPaths(new HashMap<>());
        for (Method method : pluginClass.getDeclaredMethods()) {
            Pair<String, PathItem> pathItem = toPathItem(method);
            if (pathItem != null) {
                schema.getPaths().put(pathItem.getFirst(), pathItem.getSecond());
            }
        }
        return schema;
    }


    private static Pair<String, PathItem> toPathItem(Method method) throws ChatPluginException {
        if (!method.isAnnotationPresent(com.dingtalk.open.ai.plugin.annotation.AIApi.class)) {
            return null;
        }
        AIApi aiApi = method.getAnnotation(com.dingtalk.open.ai.plugin.annotation.AIApi.class);
        if (!method.isAnnotationPresent(Graph.class)) {
            throw new ChatPluginException(ChatPluginError.GRAPH_ANNOTATION_NOT_PRESENT_ERROR);
        }
        Graph graph = method.getAnnotation(Graph.class);
        PathItem pathItem = new PathItem();
        Operation operation = new Operation();
        operation.setOperationId(aiApi.name());
        operation.setSummary(aiApi.description());
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

        Examples examples = aiApi.examples();
        if (examples.text() != null && examples.text().length != 0) {
            operation.setExamples(new TextExampleReader(examples.text()).read());
        } else if (StringUtils.isNotEmpty(examples.file())) {
            operation.setExamples(new FileExampleReader(examples.file()).read());
        }
        return new Pair<>("/v" + graph.version() + graph.resource(), pathItem);

    }

    private static Schema pojoToJsonSchema(Class<?> pojoClass) {
        Schema schema = new Schema();
        schema.setType(FieldType.OBJECT);
        schema.setProperties(new HashMap<>());
        int index = 0;
        for (Field declaredField : pojoClass.getDeclaredFields()) {
            Schema desc = new Schema();
            AIField AIField = declaredField.getAnnotation(AIField.class);
            if (AIField == null) {
                continue;
            }
            String name = StringUtils.isEmpty(AIField.name()) ? declaredField.getName() : AIField.name();
            String type = parseType(declaredField.getType());
            Boolean required = AIField.required();
            String example = AIField.example();
            desc.setType(type);
            desc.setRequired(required);
            desc.setExample(example);
            desc.setDescription(AIField.desc());
            desc.setIndex(index++);
            //system grounding优先级最高
            if (AIField.systemGrounding() != null && AIField.systemGrounding() != GroundingTag.NONE) {
                GroundingOperation option = new GroundingOperation();
                option.setUrl(AIField.systemGrounding().name());
                desc.setGrounding(option);
            } else if (AIField.graphGrounding() != null && !StringUtils.isEmpty(AIField.graphGrounding().path())) {
                GroundingOperation operation = new GroundingOperation();
                operation.setUrl(AIField.graphGrounding().path());
                operation.setMethod(AIField.graphGrounding().method());
                desc.setGrounding(operation);
            }
            schema.getProperties().put(name, desc);
        }
        return schema;
    }


    private static String parseType(Class<?> fieldKlass) {
        if (fieldKlass == Boolean.class || fieldKlass == boolean.class) {
            return FieldType.BOOLEAN;
        } else if (fieldKlass == Short.class || fieldKlass == short.class || fieldKlass == Long.class || fieldKlass == long.class || fieldKlass == Integer.class || fieldKlass == int.class) {
            return FieldType.INTEGER;
        } else if (fieldKlass.isAssignableFrom(Number.class)) {
            return FieldType.NUMBER;
        } else if (fieldKlass == String.class) {
            return FieldType.STRING;
        } else if (fieldKlass.isArray()) {
            return FieldType.ARRAY;
        }
        return FieldType.OBJECT;
    }
}
