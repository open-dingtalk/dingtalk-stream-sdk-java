package com.dingtalk.open.app.api.callback;

import java.io.Serializable;

/**
 * @author feiyin
 * @date 2023/4/3
 */
public interface OpenDingTalkCallbackListener<Request, Response> extends Serializable {

    /**
     * 执行回调
     *
     * @param request
     * @return
     */
    Response execute(Request request);
}
