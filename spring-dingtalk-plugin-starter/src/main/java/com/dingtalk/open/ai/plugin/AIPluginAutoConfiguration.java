package com.dingtalk.open.ai.plugin;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@Configuration
@EnableConfigurationProperties(PluginProperties.class)
@ConditionalOnProperty(prefix = "spring.dingtalk.plugin", name = "enabled" , havingValue = "true", matchIfMissing = true)
public class AIPluginAutoConfiguration {

    @Bean(name = "pluginContainer")
    public PluginContainer configureContainer(PluginProperties properties) {
        return new PluginContainer(properties.getClientId(), properties.getClientSecret());
    }

    @Bean
    public PluginBeanProcessor configureProcessor() {
        return new PluginBeanProcessor();
    }

    @Bean("graphDispatcher")
    public OpenDingTalkGraphAPIDispatcher configureDispatcher() {
        return new OpenDingTalkGraphAPIDispatcher();
    }

}
