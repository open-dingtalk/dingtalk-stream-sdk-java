package com.dingtalk.open.app.stream.network.api;

/**
 * @author feiyin
 * @date 2023/3/27
 */
public abstract class ServiceException extends RuntimeException {

    /**
     * 获取返回码
     *
     * @return
     */
    public abstract int getCode();

}
