package com.dingtalk.open.app.stream.network.ws;

import com.dingtalk.open.app.stream.network.api.*;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import java.net.URI;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author feiyin
 * @date 2023/4/13
 */
@Protocol(protocol = {TransportProtocol.WSS})
public class WebsocketTransportConnector implements TransportConnector {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(WebsocketTransportConnector.class);
    @Override
    public Session connect(EndPointConnection connection, ClientConnectionListener listener, ConnectOption option) throws Exception {
        LOGGER.info("[DingTalk] start websocket connection, uri={}", connection.getEndPoint().toString());
        CompletableFuture<Channel> future = new CompletableFuture<>();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(NetworkSharedResources.getNetWorkEventLoopGroup());
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                WebSocketClientProtocolConfig config = WebSocketClientProtocolConfig.newBuilder().dropPongFrames(false)
                        .webSocketUri(configureWebsocketUri(connection))
                        .handshakeTimeoutMillis(option.getTtl())
                        .dropPongFrames(false)
                        .handleCloseFrames(true)
                        .build();
                SslContext sslContext = SslContextBuilder.forClient().build();
                if (connection.getProtocol().isTls()) {
                    final SSLEngine engine = sslContext.newEngine(socketChannel.alloc());
                    final SSLParameters sslParameters = new SSLParameters();
                    sslParameters.setServerNames(Collections.singletonList(new SNIHostName(connection.getEndPoint().getHost())));
                    engine.setSSLParameters(sslParameters);
                    socketChannel.pipeline().addLast(new SslHandler(engine));
                }
                socketChannel.pipeline().addLast(new HttpClientCodec());
                socketChannel.pipeline().addLast(new HttpObjectAggregator(8 * 1024));
                socketChannel.pipeline().addLast(new WebSocketClientProtocolHandler(config));
                socketChannel.pipeline().addLast(new WebsocketIdleHandler(option.getKeepAliveIdle()));
                socketChannel.pipeline().addLast(new ProtocolConnectHandler(future, option.getTimeout()));
                socketChannel.pipeline().addLast(new KeepAliveHandler(option.getKeepAliveTimeout()));
                socketChannel.pipeline().addLast(new ProtocolFrameHandler());
                socketChannel.pipeline().addLast(new NettyClientHandler(connection.getConnectionId(), listener));
            }
        }).connect(connection.getEndPoint().getHost(), connection.getEndPoint().getPort()).addListener(future1 -> {
            if (!future1.isSuccess()) {
                LOGGER.error("[DingTalk] failed to connect proxy host, e={}", future1.cause());
            }
        });
        future.get(option.getTimeout(), TimeUnit.MILLISECONDS);
        return new WebSocketSession(future.get(), connection.getConnectionId(), option.getTtl());
    }

    private static URI configureWebsocketUri(EndPointConnection connection) throws Exception {
        return new URI(connection.getEndPoint().getScheme() + "://" + connection.getEndPoint().getHost() + ":" + connection.getEndPoint().getPort() + "/connect?ticket=" + connection.getConnectionId());
    }
}
