package com.dingtalk.open;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.ai.plugin.annotation.Examples;
import com.dingtalk.open.ai.plugin.annotation.Graph;
import com.dingtalk.open.ai.plugin.annotation.AIApi;
import com.dingtalk.open.ai.plugin.annotation.AIPlugin;
import com.dingtalk.open.ai.plugin.schema.PluginSchemaParser;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@AIPlugin(name = "会议小助手", description = "自动创建钉钉会议", version = "1.0.0")
public class CreateMeetingPlugin {

    @Graph(version ="1.0" ,resource = "/meeting/create")
    @AIApi(name = "createMeeting", description = "创建钉钉会议API", examples = @Examples(file = "fewshots.json"))
    public CreateResult createAone(CreateMeetingReq req) {
        return null;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(JSON.toJSONString(PluginSchemaParser.parseSchema(CreateMeetingPlugin.class)));
    }
}
