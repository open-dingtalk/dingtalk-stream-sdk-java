package com.dingtalk.open.app.api.protocol;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class JsonMessageConverter implements MessageConverter {

    @Override
    public <T> T convert(String data, Type type) {
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data, type);
    }
}
