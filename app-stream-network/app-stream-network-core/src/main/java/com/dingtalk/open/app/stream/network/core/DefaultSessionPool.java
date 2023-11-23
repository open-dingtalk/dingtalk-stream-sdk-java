package com.dingtalk.open.app.stream.network.core;

import com.dingtalk.open.app.stream.network.api.*;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.protocol.CommandType;
import com.dingtalk.open.app.stream.protocol.ProtocolRequestFacade;
import com.dingtalk.open.app.stream.protocol.system.SystemTopic;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接池
 *
 * @author feiyin
 * @date 2022/12/23
 */
@ThreadSafe
public class DefaultSessionPool implements SessionPool {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(DefaultSessionPool.class);
    private static final int MAX_RETRY_COUNT = 3;
    private static final int INTERVAL = 5 * 1000;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Map<String, Session> sessions;
    private final AtomicBoolean status;
    private final int maxConnections;
    private final long connectionTimeout;
    private final long connectionTTL;
    private final EndPointConnectionFactory factory;
    private final ClientConnectionListener appListener;
    private final Long keepAliveIdle;

    public DefaultSessionPool(EndPointConnectionFactory factory, int maxConnections, long ttl, long connectionTimeout, long keepAliveIdle, ClientConnectionListener appListener) {
        this.sessions = new ConcurrentHashMap<>();
        this.status = new AtomicBoolean(true);
        this.factory = factory;
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("connection-pool"));
        this.appListener = appListener;
        this.maxConnections = maxConnections;
        this.connectionTimeout = connectionTimeout;
        this.connectionTTL = ttl;
        this.keepAliveIdle = keepAliveIdle;
    }


    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(new ConnectionTask(), 0L, INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取有效连接数
     *
     * @return
     */
    private int available() {
        if (!status.get()) {
            return 0;
        }
        int available = 0;
        for (Map.Entry<String, Session> entry : sessions.entrySet()) {
            if (entry.getValue().isActive() && !entry.getValue().isGoAway()) {
                available++;
            }
        }
        return available;
    }

    private void closeSession(String connectionId) {
        if (connectionId == null) {
            return;
        }
        Session session = sessions.remove(connectionId);
        if (session != null && session.isActive()) {
            session.close();
        }
    }

    /**
     * 关闭无效链接
     */
    private void evict() {
        for (Map.Entry<String, Session> entry : sessions.entrySet()) {
            Session session = sessions.get(entry.getKey());
            if (session.isExpired() || !session.isActive()) {
                closeSession(session.getId());
            }
        }
    }

    /**
     * 关闭连接池
     */
    @Override
    public void shutdown() {
        if (status.compareAndSet(true, false)) {
            scheduledExecutorService.execute(() -> {
                for (String sessionId : sessions.keySet()) {
                    try {
                        closeSession(sessionId);
                    } catch (Exception e) {
                        LOGGER.error("[DingTalk] close session failed, connectionId={}", sessionId, e);
                    }
                }
            });
            scheduledExecutorService.execute(scheduledExecutorService::shutdown);
        }
    }

    private boolean isActive() {
        return this.status.get();
    }

    private class TransportConnectionListener implements ClientConnectionListener {

        @Override
        public void receive(Context context) {
            onRequest(context.connectionId(), context.getRequest());
            DefaultSessionPool.this.appListener.receive(context);
        }

        @Override
        public void onDisConnection(String connectionId) {
            closeSession(connectionId);
            //立刻触发建连
            DefaultSessionPool.this.appListener.onDisConnection(connectionId);
        }


        private void onRequest(String connectionId, ProtocolRequestFacade request) {
            if (request == null || request.getType() == null || request.getTopic() == null) {
                return;
            }
            if (request.getType() == CommandType.SYSTEM && SystemTopic.DISCONNECT.equals(request.getTopic())) {
                Session session = sessions.get(connectionId);
                if (session == null) {
                    return;
                }
                session.goAway();
                //发起建连
                scheduledExecutorService.execute(new ConnectionTask());
            }
        }
    }


    private class ConnectionTask implements Runnable {
        @Override
        public void run() {
            try {
                if (!isActive()) {
                    return;
                }
                evict();
                if (available() < maxConnections) {
                    EndPointConnection connection = factory.openConnection();
                    Session previous = sessions.get(connection.getConnectionId());
                    if (previous != null) {
                        if (!previous.isExpired()) {
                            return;
                        } else {
                            closeSession(previous.getId());
                        }
                    }
                    Session session = new RetryRunner<Session>(MAX_RETRY_COUNT, new ExponentialBackoffPolicy())
                            .run(() -> Connector.connect(connection, new TransportConnectionListener(), connectionTimeout, connectionTTL, keepAliveIdle));
                    if (session == null) {
                        return;
                    }
                    LOGGER.info("[DingTalk] connection is established, connectionId={}", session.getId());
                    previous = sessions.put(connection.getConnectionId(), session);
                    if (previous != null) {
                        previous.close();
                    }
                }
            } catch (Throwable e) {
                LOGGER.error("[DingTalk] establish connection failed", e);
            }
        }
    }


    private static class RetryRunner<T> {

        private final BackoffPolicy policy;

        private final AtomicInteger count;


        public RetryRunner(int max, BackoffPolicy policy) {
            this.policy = policy;
            this.count = new AtomicInteger(max);
        }

        public T run(Callable<T> callable) throws Exception {
            while (count.get() > 0) {
                count.decrementAndGet();
                try {
                    return callable.call();
                } catch (Exception e) {
                    LOGGER.error("[DingTalk] retrievable executor execute failed", e);
                    if (count.get() <= 0) {
                        throw e;
                    }
                    try {
                        Thread.sleep(this.policy.next());
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            return null;
        }

    }
}
