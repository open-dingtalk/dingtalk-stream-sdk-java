package com.dingtalk.open.ai.plugin;

import com.dingtalk.open.app.api.graph.GraphAPIMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Graph {

    /**
     * graph接口名称
     *
     * @return
     */
    String name() default "";

    /**
     * 插件描述信息
     *
     * @return
     */
    String description() default "";
    /**
     * 版本信息
     *
     * @return
     */
    String version() default "1.0";

    GraphAPIMethod method() default GraphAPIMethod.POST;

    String resource();
}
