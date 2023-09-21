package com.dingtalk.open.app.api.protocol;

import com.dingtalk.open.app.stream.protocol.ContentType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class MessageConverterMapping {

    private static final Map<ContentType, MessageConverter> MAPPING = new HashMap<>();

    static {
        MAPPING.put(ContentType.APPLICATION_JSON, new JsonMessageConverter());
    }

    public static MessageConverter getConverter(ContentType contentType) {
        return MAPPING.get(contentType);
    }

}
