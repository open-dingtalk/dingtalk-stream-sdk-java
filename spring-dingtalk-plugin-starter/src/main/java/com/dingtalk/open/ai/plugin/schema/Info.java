package com.dingtalk.open.ai.plugin.schema;

import lombok.Data;

/**
 * @author feiyin
 * @date 2023/11/9
 */
@Data
public class Info {
    /**
     * 插件名称
     */
    private String title;
    /**
     * 插件版本号
     */
    private String version;
    /**
     * 插件描述信息
     */
    private String description;

}
