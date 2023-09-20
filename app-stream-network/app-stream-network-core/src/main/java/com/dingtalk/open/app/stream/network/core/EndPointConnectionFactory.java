package com.dingtalk.open.app.stream.network.core;

import com.dingtalk.open.app.stream.network.api.EndPointConnection;

/**
 * @author feiyin
 * @date 2022/12/28
 */
public interface EndPointConnectionFactory {


    /**
     * 新建连接
     *
     * @return
     * @throws Exception
     */
    EndPointConnection openConnection() throws Exception;


}
