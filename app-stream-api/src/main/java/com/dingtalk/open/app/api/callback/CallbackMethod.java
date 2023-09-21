package com.dingtalk.open.app.api.callback;

import com.dingtalk.open.app.stream.protocol.callback.CallbackResponsePayload;

/**
 * @author feiyin
 * @date 2023/3/17
 */
interface CallbackMethod {

    /**
     * 执行回调
     *
     * @param req
     * @return
     */
    CallbackResponsePayload execute(Object req);
}
