package com.dingtalk.open.app.stream.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public enum ContentType {

    /**
     * application/json
     */
    APPLICATION_JSON("application/json");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }
    private static final Map<String,ContentType> MAPPING = new HashMap<>();

    static {
        for (ContentType value : ContentType.values()) {
            MAPPING.put(value.contentType, value);
        }
    }

    public static ContentType of(String contentType) {
        if (contentType == null) {
            return null;
        }
        return MAPPING.get(contentType.toLowerCase().trim());
    }


    public String getContentType() {
        return this.contentType;
    }
}
