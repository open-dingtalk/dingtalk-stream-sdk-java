package com.dingtalk.open.ai.plugin.schema;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author feiyin
 * @date 2023/11/29
 */
@AllArgsConstructor
@Data
public class Server {

    private String url;

    private String description;

    public Server() {

    }
}
