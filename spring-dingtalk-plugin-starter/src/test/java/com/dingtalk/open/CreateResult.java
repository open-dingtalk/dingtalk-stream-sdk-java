package com.dingtalk.open;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.ai.plugin.PluginParser;
import com.dingtalk.open.ai.plugin.annotation.FieldDesc;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class CreateResult {

    @FieldDesc(desc = "是否成功", example = "true")
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public static void main(String[] args) {
      System.out.println(JSON.toJSONString(PluginParser.parseManifest(new TestAIPlugin())));
    }
}
