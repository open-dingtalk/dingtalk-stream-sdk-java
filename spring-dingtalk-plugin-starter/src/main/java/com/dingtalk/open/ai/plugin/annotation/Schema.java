package com.dingtalk.open.ai.plugin.annotation;

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
     * 是否是必须字段
     *
     * @return
     */
    boolean required() default true;

}
