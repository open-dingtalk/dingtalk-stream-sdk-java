package com.dingtalk.open;

import com.dingtalk.open.ai.plugin.Graph;
import com.dingtalk.open.ai.plugin.annotation.AIAbility;
import com.dingtalk.open.ai.plugin.annotation.AIPlugin;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@AIPlugin(name = "aone测试插件", description = "插件描述信息")
public class TestAIPlugin {


    @Graph(resource = "/aone/get")
    @AIAbility(name = "创建aone", description = "创建aone", fewShots = {})
    public CreateResult createAone(AoneCreateReq req) {
        return null;
    }

}
