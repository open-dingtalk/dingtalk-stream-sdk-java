package com.dingtalk.open.app.api.open;

/**
 * @author feiyin
 * @date 2023/2/9
 */
public interface OpenApiClient {
    /**
     * 创建GateWay endpoint
     *
     * @param request
     * @return
     * @throws Exception
     */
    OpenConnectionResponse openConnection(OpenConnectionRequest request) throws Exception;


}
