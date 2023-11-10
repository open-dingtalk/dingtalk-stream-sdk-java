package com.dingtalk.open.ai.plugin;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.ai.plugin.schema.Example;

/**
 * @author feiyin
 * @date 2023/11/10
 */
public class TextExampleReader implements ExampleReader {
    private final String[] examples;

    public TextExampleReader(String[] examples) {
        this.examples = examples;
    }

    @Override
    public Example[] read() {
        if (examples == null || examples.length == 0) {
            return new Example[]{};
        }

        Example[] result = new Example[examples.length];

        for (int i = 0; i < examples.length; i++) {
            result[i] = JSON.parseObject(examples[i], Example.class);
        }
        return result;
    }
}
