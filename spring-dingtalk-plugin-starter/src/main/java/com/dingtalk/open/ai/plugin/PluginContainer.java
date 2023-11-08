package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dingtalk.open.ai.plugin.annotation.AIPlugin;
import com.dingtalk.open.ai.plugin.api.ReportOpenApiService;
import com.dingtalk.open.ai.plugin.api.ReportPluginRequest;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.callback.DingTalkStreamTopics;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Dispatcher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class PluginContainer implements ApplicationListener<ContextRefreshedEvent> {

    private String clientId;

    private String clientSecret;


    @Autowired
    private GraphDispatcher dispatcher;


    private final List<AIPluginReporter> reporters = new LinkedList<>();


    public void register(AIPluginReporter reporter) {
        reporters.add(reporter);
    }


    public PluginContainer(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        reporters.forEach(aiPluginReporter -> {
            ReportPluginRequest request = new ReportPluginRequest();
            request.setClientId(clientId);
            AIPlugin aiPlugin = aiPluginReporter.getPlugin();
            request.setDescription(aiPlugin.description());
            request.setName(aiPlugin.name());
            request.setVersion(aiPlugin.version());
            request.setManifest(PluginParser.parseManifest(aiPluginReporter.getTargetClass()));
            try {
                ReportOpenApiService.report(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            OpenDingTalkStreamClientBuilder.custom().preEnv()
                    .credential(new AuthClientCredential(clientId, clientSecret))
                    .registerCallbackListener(DingTalkStreamTopics.GRAPH_API_TOPIC, dispatcher).build()
                    .start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
