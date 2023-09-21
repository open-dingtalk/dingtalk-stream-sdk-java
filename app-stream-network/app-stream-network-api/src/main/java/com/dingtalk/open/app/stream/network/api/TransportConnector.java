package com.dingtalk.open.app.stream.network.api;

/**
 * @author feiyin
 * @date 2023/4/13
 */
public interface TransportConnector {

    /**
     * 建连
     *
     * @param connection
     * @param listener
     * @param option
     * @return
     * @throws Exception
     */
    Session connect(EndPointConnection connection, ClientConnectionListener listener, ConnectOption option) throws Exception;
}
