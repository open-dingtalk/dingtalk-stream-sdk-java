package com.dingtalk.open.ai.plugin;

import com.dingtalk.open.ai.plugin.schema.Example;

/**
 * @author feiyin
 * @date 2023/11/10
 */
public interface ExampleReader {

    Example[] read() throws Exception;
}
