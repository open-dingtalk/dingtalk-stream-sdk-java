package com.dingtalk.open.ai.plugin.schema;

import lombok.Data;

/**
 * @author feiyin
 * @date 2023/11/9
 */
@Data
public class PathItem {

    private Operation get;

    private Operation post;

}
