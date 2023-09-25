package com.dingtalk.open.app.stream.network.api;

/**
 * @author feiyin
 * @date 2023/9/25
 */
public enum ServiceType {

    /**
     * 单向请求
     */
    UNARY,
    /**
     * 请求是流，单个响应
     */
    STREAM_REQUEST,
    /**
     * 响应是流
     */
    STREAM_RESPONSE,
    /**
     * 双向流
     */
    BIDIRECTIONAL_STREAM
}
