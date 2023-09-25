package com.dingtalk.open.app.stream.network.api.exception;

/**
 * @author feiyin
 * @date 2023/1/5
 */
public enum NetWorkError {

    /**
     * 建连失败
     */
    CONNECT_ERROR,

    /**
     * 创建连接失败
     */
    OPEN_CONNECTION_ERROR,


    UNKNOWN_PROTOCOL,

    /**
     * 非法协议
     */
    PROTOCOL_ILLEGAL,

    /**
     * 不支持的协议
     */
    UNSUPPORTED_PROTOCOL,


}
