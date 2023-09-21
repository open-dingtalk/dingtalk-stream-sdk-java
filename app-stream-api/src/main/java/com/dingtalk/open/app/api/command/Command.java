package com.dingtalk.open.app.api.command;

import com.dingtalk.open.app.stream.protocol.ProtocolResponse;
import com.dingtalk.open.app.stream.protocol.ProtocolRequest;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public interface Command {

    /**
     * 处理数据
     *
     * @param request
     * @return
     */
    ProtocolResponse execute(ProtocolRequest request);

}
