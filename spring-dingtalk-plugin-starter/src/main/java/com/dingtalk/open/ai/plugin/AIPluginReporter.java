package com.dingtalk.open.ai.plugin;

import com.dingtalk.open.ai.plugin.annotation.AIPlugin;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class AIPluginReporter {

    private PluginContainer container;

    private Class targetClass;

    /**
     * 注册当前数据
     */
    public void register() {
        container.register(this);
    }


    public void setContainer(PluginContainer container) {
        this.container = container;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public Class getTargetClass() {
        return targetClass;
    }


    public AIPlugin getPlugin() {
        return (AIPlugin) targetClass.getAnnotation(AIPlugin.class);
    }
}
