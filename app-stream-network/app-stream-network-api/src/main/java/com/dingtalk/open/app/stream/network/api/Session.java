package com.dingtalk.open.app.stream.network.api;

/**
 * @author feiyin
 * @date 2023/1/13
 */
public interface Session {

    /**
     * 判断连接是否可用
     *
     * @return
     */
    boolean isActive();
    /**
     * 断开连接
     */
    void close();
    /**
     * 获取id
     *
     * @return
     */
    String getId();
    /**
     * 建连时间
     *
     * @return
     */
    boolean isExpired();

    /**
     * 优雅关闭
     */
    void goAway();


    /**
     * 是否是goAway
     *
     * @return
     */
    boolean isGoAway();

}
