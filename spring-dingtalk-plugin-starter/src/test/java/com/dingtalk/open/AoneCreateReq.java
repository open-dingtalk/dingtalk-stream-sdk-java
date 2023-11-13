package com.dingtalk.open;

import com.dingtalk.open.ai.plugin.GroundingTag;
import com.dingtalk.open.ai.plugin.annotation.AIField;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class AoneCreateReq {

    @AIField(desc = "aone项目id", example = "12321")
    private Long akProjectId;

    @AIField(desc = "创建人", example = "飞隐", systemGrounding = GroundingTag.SYSTEM_UNION_GROUNDING)
    private String author;

    @AIField(desc = "创建人", example = "飞隐", systemGrounding = GroundingTag.SYSTEM_UNION_GROUNDING)
    private String assignTo;

    @AIField(desc = "创建人", example = "飞隐")
    private String subject;

    public Long getAkProjectId() {
        return akProjectId;
    }

    public String getAuthor() {
        return author;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public String getSubject() {
        return subject;
    }
}
