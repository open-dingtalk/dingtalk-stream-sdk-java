package com.dingtalk.open.app.api.protocol;

import java.lang.reflect.Type;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public interface MessageConverter {

    /**
     * 转换器
     *
     * @param data
     * @param type
     * @param <T>
     * @return
     */
    <T> T convert(String data, Type type);


}
