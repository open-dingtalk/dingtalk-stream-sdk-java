package com.dingtalk.open.ai.plugin.schema;

import lombok.Data;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/11/9
 */
@Data
public class Response {

    private Map<String, MediaType> content;

}
