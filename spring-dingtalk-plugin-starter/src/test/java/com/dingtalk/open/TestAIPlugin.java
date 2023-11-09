package com.dingtalk.open;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.ai.plugin.Graph;
import com.dingtalk.open.ai.plugin.annotation.AIAbility;
import com.dingtalk.open.ai.plugin.annotation.AIPlugin;
import com.dingtalk.open.ai.plugin.parser.PluginSchemaParser;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@AIPlugin(name = "aone测试插件", description = "aone测试插件")
public class TestAIPlugin {


    @Graph(name ="问答小助手" ,resource = "/aone/get", description = "")
    @AIAbility(name = "创建aone", description = "创建aone", fewShots = {})
    public CreateResult createAone(AoneCreateReq req) {
        return null;
    }


    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(PluginSchemaParser.parseManifest(TestAIPlugin.class)));
    }

}
