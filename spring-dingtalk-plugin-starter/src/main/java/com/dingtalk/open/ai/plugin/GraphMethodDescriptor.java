package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.ai.plugin.annotation.Graph;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author feiyin
 * @date 2023/11/6
 */
public class GraphMethodDescriptor {

    private Type parameterType;

    private Method method;

    private Object target;

    private GraphDispatcher dispatcher;

    @SuppressWarnings("unchecked")
    public GraphMethodDescriptor() {
    }


    @SuppressWarnings("unchecked")
    public String invoke(String body) throws Exception {
        return JSON.toJSONString(method.invoke(target, new Object[]{JSON.parseObject(body, parameterType)}));
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void register() {
        this.parameterType = method.getParameterTypes()[0];
        Graph graphPost = method.getAnnotation(Graph.class);
        dispatcher.register(graphPost.method(), graphPost.version(), graphPost.resource(), this);
    }

    public void setDispatcher(GraphDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
}
