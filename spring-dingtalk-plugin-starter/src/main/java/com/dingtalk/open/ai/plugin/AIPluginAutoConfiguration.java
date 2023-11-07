package com.dingtalk.open.ai.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@Configuration
@EnableConfigurationProperties(AIProperties.class)
public class AIPluginAutoConfiguration {


    @Autowired
    private AIProperties properties;


    @Bean(name = "pluginContainer")
    public PluginContainer configureContainer() {
        return new PluginContainer(properties.getClientId(), properties.getClientSecret());
    }

    @Bean
    public PluginBeanProcessor configureProcessor() {
        return new PluginBeanProcessor();
    }

    @Bean("graphDispatcher")
    public GraphDispatcher configureDispatcher() {
        return new GraphDispatcher();
    }

}
