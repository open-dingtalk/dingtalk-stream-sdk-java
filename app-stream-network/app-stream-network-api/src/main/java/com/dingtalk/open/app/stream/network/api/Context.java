package com.dingtalk.open.app.stream.network.api;

import com.dingtalk.open.app.stream.protocol.ProtocolRequestFacade;

import java.util.concurrent.Executor;

/**
 * @author feiyin
 * @date 2022/12/28
 */
public interface Context {
    /**
     * 响应
     *
     * @param payload
     * @param endOfStream
     */
    void streamReply(Object payload, boolean endOfStream);

    /**
     * 异常
     *
     * @param t
     */
    void exception(Throwable t);

    /**
     * 回话id
     *
     * @return
     */
    String getConnectionId();

    /**
     * 获取请求
     *
     * @return
     */
    ProtocolRequestFacade getRequest();

    /**
     * 获取业务线程池
     *
     * @return
     */
    Executor getBizExecutor();

    /**
     * 设置业务线程池
     *
     * @param executor
     */
    void setBizExecutor(Executor executor);

}
