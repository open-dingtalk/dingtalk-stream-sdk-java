package com.dingtalk.open;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.ai.plugin.annotation.Examples;
import com.dingtalk.open.ai.plugin.annotation.Graph;
import com.dingtalk.open.ai.plugin.annotation.AIApi;
import com.dingtalk.open.ai.plugin.annotation.AIPlugin;
import com.dingtalk.open.ai.plugin.parser.PluginSchemaParser;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@AIPlugin(name = "aone测试插件", description = "aone测试插件")
public class TestAIPlugin {

    @Graph(resource = "/aone/create/issue")
    @AIApi(
            name = "createAone",
            description = "创建aone",
            examples = @Examples(file= "fewshots.json")
    )
    public CreateResult createAone(AoneCreateReq req) {
        return null;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(JSON.toJSONString(PluginSchemaParser.parseManifest(TestAIPlugin.class)));
    }

}
