package com.dingtalk.open.app.api.callback;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.api.Preconditions;

import java.lang.reflect.Type;

/**
 * @author feiyin
 * @date 2023/3/17
 */
class JsonMarshaller implements Marshaller{
    private final Type type;

    public JsonMarshaller(Type type) {
        this.type = Preconditions.notNull(type);
    }
    @Override
    public <T> T marshal(Object data) {
        return JSON.parseObject(JSON.toJSONString(data), type);
    }
}
