package com.dingtalk.open.app.stream.protocol;

/**
 * @author feiyin
 * @date 2023/4/7
 */
public interface ProtocolRequestFacade {
    /**
     * 获取messageId
     *
     * @return messageId
     */
    String getMessageId();

    /**
     * 获取传输类型
     *
     * @return contentType
     */
    String getContentType();

    /**
     * 获取topic
     *
     * @return topic
     */
    String getTopic();

    /**
     * 获取执行类型
     *
     * @return type
     */
    CommandType getType();

    /**
     * 获取数据
     *
     * @return data
     */
    String getData();

    /**
     * 获取名称
     *
     * @param headerName
     * @return header
     */
    String getHeader(String headerName);


    /**
     * 获取请求
     *
     * @return request
     */
    ProtocolRequest getRequest();

}
