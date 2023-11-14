package com.dingtalk.open.ai.plugin.annotation;

import com.dingtalk.open.ai.plugin.GroundingTag;
import com.dingtalk.open.app.api.graph.GraphAPIMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Schema {


    String name() default "";

    /**
     * 举例
     *
     * @return
     */
    String example() default "";

    /**
     * 描述信息
     *
     * @return
     */
    String desc();

    /**
     * 系统grounding
     *
     * @return
     */
    GroundingTag systemGrounding() default GroundingTag.NONE;

    /**
     * 接口grounding
     *
     * @return
     */
    GraphGrounding graphGrounding() default @GraphGrounding(path = "", method = GraphAPIMethod.GET);

    /**
     * 是否是必须字段
     *
     * @return
     */
    boolean required() default true;

}
