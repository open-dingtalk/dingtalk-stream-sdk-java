package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson2.JSON;
import com.dingtalk.open.ai.plugin.schema.Example;
import com.dingtalk.open.app.api.util.IoUtils;

/**
 * @author feiyin
 * @date 2023/11/10
 */
public class FileExampleReader implements ExampleReader {

    private final String path;

    public FileExampleReader(String path) {
        this.path = path;
    }

    @Override
    public Example[] read() throws Exception {

        byte [] content =IoUtils.readAll(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
        return JSON.parseObject(content, Example[].class);
    }

}
