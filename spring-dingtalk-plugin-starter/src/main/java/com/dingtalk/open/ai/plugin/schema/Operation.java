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
    @JSONField(name = "x-examples")
    private Example[] examples;

}
