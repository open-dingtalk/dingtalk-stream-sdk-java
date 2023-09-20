package com.dingtalk.open.app.api.callback;

import java.io.Serializable;

/**
 * @author feiyin
 * @date 2023/4/3
 */
public interface OpenDingTalkCallbackListener<Input, Output> extends Serializable {

    /**
     * 执行回调
     *
     * @param param
     * @return
     */
    Output execute(Input param);
}
