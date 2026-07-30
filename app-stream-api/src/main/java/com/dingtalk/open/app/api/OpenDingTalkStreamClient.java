package com.dingtalk.open.app.api;

import com.dingtalk.open.app.api.command.CommandDispatcher;
import com.dingtalk.open.app.api.open.OpenApiClient;
import com.dingtalk.open.app.api.open.OpenApiClientBuilder;
import com.dingtalk.open.app.api.open.OpenConnectionRequest;
import com.dingtalk.open.app.api.open.OpenConnectionResponse;
import com.dingtalk.open.app.api.protocol.AppServiceListener;
import com.dingtalk.open.app.api.security.DingTalkCredential;
import com.dingtalk.open.app.api.util.IpUtils;
import com.dingtalk.open.app.stream.network.api.ClientConnectionListener;
import com.dingtalk.open.app.stream.network.api.EndPointConnection;
import com.dingtalk.open.app.stream.network.api.NetProxy;
import com.dingtalk.open.app.stream.network.api.NetworkSharedResources;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;
import com.dingtalk.open.app.stream.network.core.EndPointConnectionFactory;
import com.dingtalk.open.app.stream.network.core.NetWorkService;
import com.dingtalk.open.app.stream.network.core.Subscription;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author feiyin
 * @date 2022/12/26
 */
class OpenDingTalkStreamClient implements OpenDingTalkClient {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(OpenDingTalkStreamClient.class);
    private static final long EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS = 5L;
    private final DingTalkCredential credential;
    private final CommandDispatcher dispatcher;
    private final ExecutorService executor;
    private final ClientOption option;
    private final AtomicReference<Status> status;
    private final NetProxy netProxy;
    private NetWorkService netWorkService;
    private OpenApiClient openApiClient;
    private Set<Subscription> subscriptions;
    private boolean networkResourcesAcquired;

    public OpenDingTalkStreamClient(DingTalkCredential credential, CommandDispatcher dispatcher, ExecutorService executor, ClientOption option, Set<Subscription> subscriptions,
                                    NetProxy netProxy) {
        this.credential = credential;
        this.dispatcher = dispatcher;
        this.executor = executor;
        this.option = option;
        this.subscriptions = Collections.unmodifiableSet(new HashSet<>(subscriptions));
        this.status = new AtomicReference<>(Status.INIT);
        this.netProxy = netProxy;
    }

    @Override
    public synchronized void start() throws OpenDingTalkAppException {
        if (status.get() == Status.INIT) {
            NetworkSharedResources.acquireNetWorkEventLoopGroup();
            networkResourcesAcquired = true;
            try {
                this.openApiClient = OpenApiClientBuilder.create()
                        .setHost(option.getOpenApiHost())
                        .setTimeout(toHttpTimeout(option.getConnectTimeout()))
                        .setProxy(netProxy)
                        .build();
                final EndPointConnectionFactory factory = () -> openConnection(this.credential, subscriptions, netProxy);
                ClientConnectionListener listener = new AppServiceListener(dispatcher, executor);
                this.netWorkService = new NetWorkService(factory, listener, option.getMaxConnectionCount(), option.getConnectionTTL(), option.getConnectTimeout(), option.getKeepAliveOption().getKeepAliveIdleMill());
                this.netWorkService.start();
                this.status.set(Status.ACTIVE);
            } catch (RuntimeException | Error e) {
                try {
                    if (this.netWorkService != null) {
                        this.netWorkService.shutdown();
                    }
                } catch (Exception shutdownError) {
                    e.addSuppressed(shutdownError);
                } finally {
                    shutdownConsumerExecutor();
                    releaseNetworkResources();
                    status.set(Status.INACTIVE);
                }
                throw e;
            }
        } else if (status.get() == Status.INACTIVE) {
            throw new OpenDingTalkAppException(DingTalkAppError.CLIENT_STATE_ERROR);
        }
    }

    @Override
    public synchronized void stop() throws Exception {
        if (status.get() != Status.INACTIVE) {
            try {
                if (this.netWorkService != null) {
                    this.netWorkService.shutdown();
                }
            } finally {
                try {
                    shutdownConsumerExecutor();
                } finally {
                    releaseNetworkResources();
                    status.set(Status.INACTIVE);
                }
            }
        }
    }

    private void shutdownConsumerExecutor() {
        if (executor == null) {
            return;
        }
        this.executor.shutdownNow();
        try {
            if (!this.executor.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                LOGGER.warn("[DingTalk] consumer executor did not terminate within {} seconds",
                        EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("[DingTalk] interrupted while waiting for consumer executor shutdown");
        }
    }

    private void releaseNetworkResources() {
        if (networkResourcesAcquired) {
            NetworkSharedResources.releaseNetWorkEventLoopGroup();
            networkResourcesAcquired = false;
        }
    }

    static int toHttpTimeout(long timeout) {
        return (int) Math.min(timeout, Integer.MAX_VALUE);
    }

    private EndPointConnection openConnection(DingTalkCredential credential, Set<Subscription> subscriptions, NetProxy proxy) throws Exception {
        OpenConnectionRequest request = new OpenConnectionRequest();
        request.setClientId(credential.getClientId());
        request.setClientSecret(credential.getClientSecret());
        request.setUa(UserAgent.getUserAgent().getUa());
        request.setSubscriptions(subscriptions);
        request.setLocalIp(IpUtils.getLocalIP());
        OpenConnectionResponse response = openApiClient.openConnection(request);
        return new EndPointConnection(credential.getClientId(), response.getEndpoint(), response.getTicket(), proxy);
    }


    enum Status {
        /**
         * 初始化
         */
        INIT,
        /**
         * 启动
         */
        ACTIVE,
        /**
         * 关闭
         */
        INACTIVE

    }
}
