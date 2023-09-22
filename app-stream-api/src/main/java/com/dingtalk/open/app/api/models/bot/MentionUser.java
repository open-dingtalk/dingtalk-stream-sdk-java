package com.dingtalk.open.app.api.models.bot;

import java.io.Serializable;

public class MentionUser implements Serializable {
    private static final long serialVersionUID = 1L;
    String dingtalkId;
    String staffId;

    public String getDingtalkId() {
        return dingtalkId;
    }

    public void setDingtalkId(String dingtalkId) {
        this.dingtalkId = dingtalkId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
