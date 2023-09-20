package com.dingtalk.open.app.api.callback;

/**
 * @author feiyin
 * @date 2023/3/17
 */
interface Marshaller {

    /**
     * 序列化
     *
     * @param data
     * @param <T>
     * @return
     */
    <T> T marshal(Object data);
}
