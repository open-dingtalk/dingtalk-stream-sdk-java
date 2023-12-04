package com.dingtalk.open.ai.plugin.schema;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/11/9
 */
@Data
public class OpenAPI {

    /**
     * api名称
     */
    private String openapi;
    /**
     * 插件描述信息
     */
    private Info info;
    /**
     * 服务器信息
     */
    private Server[] servers;
    /**
     * 路径信息
     */
    private Map<String, PathItem> paths;

    @JSONField(name = "x-dingtalk-phrases")
    private String [] keywords;


}
