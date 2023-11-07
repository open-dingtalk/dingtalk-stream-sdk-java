package com.dingtalk.open;

import com.dingtalk.open.ai.plugin.GroundingTag;
import com.dingtalk.open.ai.plugin.annotation.FieldDesc;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class AoneCreateReq {

    @FieldDesc(desc = "aone项目id", example = "12321")
    private Long akProjectId;

    @FieldDesc(desc = "创建人", example = "飞隐", grounding = GroundingTag.SYSTEM_UNION_GROUNDING)
    private String author;

    @FieldDesc(desc = "创建人", example = "飞隐", grounding = GroundingTag.SYSTEM_UNION_GROUNDING)
    private String assignTo;

    @FieldDesc(desc = "创建人", example = "飞隐")
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
