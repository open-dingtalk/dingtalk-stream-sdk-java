package com.dingtalk.open.ai.plugin.annotation;

import com.dingtalk.open.app.api.graph.GraphAPIMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author feiyin
 * @date 2023/11/9
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GraphGrounding {

    GraphAPIMethod method();

    String path();


}
