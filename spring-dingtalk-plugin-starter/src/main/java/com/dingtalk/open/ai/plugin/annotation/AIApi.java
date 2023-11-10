package com.dingtalk.open.ai.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author feiyin
 * @date 2023/11/6
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIApi {

    /**
     * api能力名称
     *
     * @return
     */
    String name();

    /**
     * 插件描述信息
     *
     * @return
     */
    String description();

    /**
     * 提示词
     *
     * @return
     */
    Examples examples();

}
