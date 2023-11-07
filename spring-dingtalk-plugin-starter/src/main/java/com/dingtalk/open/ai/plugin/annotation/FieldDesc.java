package com.dingtalk.open.ai.plugin.annotation;

import com.dingtalk.open.ai.plugin.GroundingTag;

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
public @interface FieldDesc {


    String name() default "";

    /**
     * 举例
     *
     * @return
     */
    String example();

    /**
     * 描述信息
     *
     * @return
     */
    String desc();

    /**
     * 字段grounding
     *
     * @return
     */
    GroundingTag grounding() default GroundingTag.NONE;


    boolean required() default true;

}
