package com.dingtalk.open.app.api.protocol;

import com.dingtalk.open.app.stream.network.api.Context;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public interface CommandExecutor {

    /**
     * 执行命令
     *
     * @param context
     */
    void execute(Context context);

}
