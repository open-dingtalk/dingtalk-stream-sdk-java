package com.dingtalk.open.app.stream.network.api;

/**
 * 数据监听器
 *
 * @author feiyin
 * @date 2022/12/28
 */
public interface ClientConnectionListener {
    /**
     * 获取消息
     *
     * @param context
     */
    void receive(Context context);

    /**
     * 连接断开
     *
     * @param connectionId
     */
    default void onDisConnection(String connectionId) {
    }
}
