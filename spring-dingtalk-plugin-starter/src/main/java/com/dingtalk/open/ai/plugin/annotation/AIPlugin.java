package com.dingtalk.open.ai.plugin.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author feiyin
 * @date 2023/11/6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Inherited
public @interface AIPlugin {

    /**
     * AI插件名称
     *
     * @return
     */
    String name();

    /**
     * AI插件描述信息
     *
     * @return
     */
    String description();

    /**
     * 插件版本,默认1.0.0
     *
     * @return
     */
    String version() default "1.0.0";

}
