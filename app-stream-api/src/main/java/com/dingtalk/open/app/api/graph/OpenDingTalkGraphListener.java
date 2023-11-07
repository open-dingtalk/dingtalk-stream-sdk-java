package com.dingtalk.open.app.api.graph;

/**
 * @author feiyin
 * @date 2023/11/6
 */
public interface OpenDingTalkGraphListener<Req, Resp> {

    /**
     * graph请求
     *
     * @param resp
     * @return
     */
    Resp execute(Req resp);

}
