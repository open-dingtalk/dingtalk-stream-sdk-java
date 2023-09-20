package com.dingtalk.open.app.stream.network.core;

/**
 * @author feiyin
 * @date 2023/2/28
 */
public interface BackoffPolicy {

    /**
     * 返回退避事件 毫秒
     *
     * @return
     */
    long next();

}
