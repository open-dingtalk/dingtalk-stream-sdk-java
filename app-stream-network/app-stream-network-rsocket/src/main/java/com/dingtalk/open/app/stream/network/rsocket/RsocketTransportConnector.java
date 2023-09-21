package com.dingtalk.open.app.stream.network.rsocket;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.stream.network.api.*;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;

/**
 * @author feiyin
 * @date 2023/4/13
 */
@Protocol(protocol = TransportProtocol.RSOCKET_TCP)
public class RsocketTransportConnector implements TransportConnector {

    @Override
    public Session connect(EndPointConnection connection, ClientConnectionListener listener, ConnectOption option) throws Exception {
        Sinks.One<RSocketPushSession> one = Sinks.one();
        RSocketConnector.create().acceptor((setup, sendingSocket) -> {
            RSocket receiver = new ReceiverSocket(connection.getConnectionId(), listener);
            one.tryEmitValue(new RSocketPushSession(connection.getConnectionId(), receiver, sendingSocket, option.getTtl()));
            return Mono.just(receiver);
        }).setupPayload(setupPayload(connection)).keepAlive(option.getKeepAliveIdle(), option.getKeepAliveTimeout()).connect(TcpClientTransport.create(TcpClient.create().host(connection.getEndPoint().getHost()).port(connection.getEndPoint().getPort()).runOn(NetworkSharedResources.getNetWorkEventLoopGroup()).secure())).timeout(Duration.ofMillis(option.getTimeout())).block();
        return one.asMono().block();
    }

    private static Payload setupPayload(EndPointConnection connection) {
        SetupPayload setupPayload = new SetupPayload(connection.getConnectionId());
        return DefaultPayload.create(JSON.toJSONBytes(setupPayload));
    }
}
