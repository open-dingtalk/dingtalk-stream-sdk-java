package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.ai.plugin.api.ReportOpenApiService;
import com.dingtalk.open.ai.plugin.api.ReportPluginRequest;
import com.dingtalk.open.ai.plugin.schema.PluginSchemaParser;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.callback.DingTalkStreamTopics;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class PluginContainer implements ApplicationListener<ContextRefreshedEvent> {

    private String clientId;

    private String clientSecret;

    @Autowired
    private OpenDingTalkGraphAPIDispatcher dispatcher;

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
            try {
                request.setManifest(JSON.toJSONString(PluginSchemaParser.parseSchema(aiPluginReporter.getTargetClass())));
                ReportOpenApiService.report(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            OpenDingTalkStreamClientBuilder builder = OpenDingTalkStreamClientBuilder.custom();
            if (Env.isPre()) {
                builder.preEnv();
            }
            builder.credential(new AuthClientCredential(clientId, clientSecret)).registerCallbackListener(DingTalkStreamTopics.GRAPH_API_TOPIC, dispatcher).build().start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
