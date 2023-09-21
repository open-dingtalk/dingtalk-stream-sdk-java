package com.dingtalk.open.app.api;

/**
 * 钉钉客户端
 *
 * @author feiyin
 * @date 2022/12/23
 */
public interface OpenDingTalkClient {
    /**
     * 启动客户端
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 关闭客户端
     *
     * @throws Exception
     */
    void stop() throws Exception;

}
