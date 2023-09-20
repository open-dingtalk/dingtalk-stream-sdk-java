package com.dingtalk.open.app.stream.network.api;

import com.dingtalk.open.app.stream.protocol.ProtocolRequestFacade;

/**
 * @author feiyin
 * @date 2022/12/28
 */
public interface Context {
    /**
     * 响应
     *
     * @param payload
     */
    void replay(Object payload);

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
    String connectionId();

    /**
     * 获取请求
     *
     * @return
     */
    ProtocolRequestFacade getRequest();

}
