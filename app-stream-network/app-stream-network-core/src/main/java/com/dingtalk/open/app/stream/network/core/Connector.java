package com.dingtalk.open.app.stream.network.core;

import com.dingtalk.open.app.stream.network.api.*;
import com.dingtalk.open.app.stream.network.api.exception.DingTalkNetworkException;
import com.dingtalk.open.app.stream.network.api.exception.NetWorkError;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author feiyin
 * @date 2023/1/13
 */
public class Connector {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(Connector.class);
    private static final Duration KEEP_ALIVE_TIMEOUT = Duration.ofMillis(5 * 1000L);
    private static final Map<TransportProtocol, TransportConnector> CONNECTOR_REGISTRY = new ConcurrentHashMap<>();
    private static final Lock INIT_LOCK = new ReentrantLock();
    private static volatile Boolean INIT = false;
    /**
     * 建立连接
     *
     * @param connection
     * @param listener
     * @param timeout
     * @param ttl
     * @param keepAliveIdle
     * @return
     * @throws Exception
     */
    public static Session connect(EndPointConnection connection, ClientConnectionListener listener, long timeout, long ttl, long keepAliveIdle) throws Exception {
        ensureActive();
        TransportConnector connector = CONNECTOR_REGISTRY.get(connection.getProtocol());
        if (connector == null) {
            throw new DingTalkNetworkException(NetWorkError.PROTOCOL_ILLEGAL);
        }
        ConnectOption option = ConnectOption.builder().setTimeout(timeout).setTtl(ttl).setKeepAliveIdle(Duration.ofMillis(keepAliveIdle)).setKeepAliveTimeout(KEEP_ALIVE_TIMEOUT).build();
        return connector.connect(connection, listener, option);
    }


    private static void ensureActive() {
        if (INIT) {
            return;
        }
        INIT_LOCK.lock();
        try {
            if (INIT) {
                return;
            }
            ServiceLoader<TransportConnector> transportConnectors = ServiceLoader.load(TransportConnector.class);
            Iterator<TransportConnector> it = transportConnectors.iterator();
            while (it.hasNext()) {
                TransportConnector transportConnector = it.next();
                Protocol protocols = transportConnector.getClass().getAnnotation(Protocol.class);
                if (protocols != null) {
                    TransportProtocol[] supportedProtocols = protocols.protocol();
                    for (TransportProtocol supportedProtocol : supportedProtocols) {
                        CONNECTOR_REGISTRY.put(supportedProtocol, transportConnector);
                    }
                }
            }
            INIT = true;
        } catch (Exception e) {
            LOGGER.error("[DingTalk] client init transport failed", e);
        } finally {
            INIT_LOCK.unlock();
        }
    }
}
