package com.dingtalk.open;

import com.dingtalk.open.ai.plugin.annotation.FieldDesc;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class CreateResult {

    @FieldDesc(desc = "是否成功")
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
