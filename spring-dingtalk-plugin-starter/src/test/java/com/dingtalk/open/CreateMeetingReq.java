package com.dingtalk.open;

import com.dingtalk.open.ai.plugin.GroundingTag;
import com.dingtalk.open.ai.plugin.annotation.Schema;
import lombok.Data;

/**
 * @author feiyin
 * @date 2023/11/7
 */
@Data
public class CreateMeetingReq {

    @Schema(desc = "创建人", example = "飞隐", systemGrounding = GroundingTag.SYSTEM_UNION_ID)
    private String creator;

    @Schema(desc = "参会人", example = "[张三, 李四]", systemGrounding = GroundingTag.SYSTEM_UNION_ID)
    private String[] participants;

    @Schema(desc = "创建人", example = "飞隐", systemGrounding = GroundingTag.SYSTEM_TIME_POINT)
    private String time;

}
