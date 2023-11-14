package com.dingtalk.open;

import com.dingtalk.open.ai.plugin.annotation.Schema;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class CreateResult {

    @Schema(desc = "是否成功")
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
