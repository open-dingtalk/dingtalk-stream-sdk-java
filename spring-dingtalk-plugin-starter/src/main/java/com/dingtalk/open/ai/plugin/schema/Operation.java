package com.dingtalk.open.ai.plugin.schema;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/11/9
 */
@Data
public class Operation {
    /**
     * 描述信息
     */
    private String summary;
    /**
     * 接口id
     */
    private String operationId;

    @JSONField(name = "x-dingtalk-confirmParams")
    private Boolean confirmParams;
    /**
     * 请求body
     */
    private RequestBody requestBody;

    /**
     * response
     */
    private Map<String, Response> responses;
    /**
     * 示例
     */
    @JSONField(name = "x-dingtalk-examples")
    private Example[] examples;

}
