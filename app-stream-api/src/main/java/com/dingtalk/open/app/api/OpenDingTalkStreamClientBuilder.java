package com.dingtalk.open.app.api;

import com.dingtalk.open.app.api.command.CommandDispatcher;
import com.dingtalk.open.app.api.graph.GraphAPIMethod;
import com.dingtalk.open.app.api.graph.OpenDingTalkGraphListener;
import com.dingtalk.open.app.api.util.ThreadUtil;
import com.dingtalk.open.app.api.callback.CallbackCommandExecutor;
import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import com.dingtalk.open.app.api.protocol.CommandExecutor;
import com.dingtalk.open.app.api.protocol.EventCommandExecutor;
import com.dingtalk.open.app.api.security.DingTalkCredential;
import com.dingtalk.open.app.stream.network.core.Subscription;
import com.dingtalk.open.app.stream.protocol.CommandType;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author feiyin
 * @date 2023/1/3
 */
public class OpenDingTalkStreamClientBuilder {
    private int consumeThreads = 16;
    private final Map<CommandType, CommandExecutor> commands = new HashMap<>();
    private final Set<Subscription> subscriptions = new HashSet<>();
    private DingTalkCredential credential;
    private int maxConnectionCount = 1;
    private final int connectionTimeToLive = 6 * 60 * 60 * 1000;
    private long connectTimeout = 3 * 1000L;

    private KeepAliveOption keepAliveOption = new KeepAliveOption();

    private String openApiHost = "https://api.dingtalk.com";

    private OpenDingTalkStreamClientBuilder() {
    }

    public static OpenDingTalkStreamClientBuilder custom() {
        return new OpenDingTalkStreamClientBuilder();
    }

    public OpenDingTalkStreamClientBuilder consumeThreads(int threads) {
        this.consumeThreads = Preconditions.checkPositive(threads);
        return this;
    }

    public OpenDingTalkStreamClientBuilder registerAllEventListener(GenericEventListener listener) {
        Preconditions.notNull(listener);
        this.commands.put(CommandType.EVENT, new EventCommandExecutor(Preconditions.notNull(listener)));
        subscribe(CommandType.EVENT, "*");
        return this;
    }

    public OpenDingTalkStreamClientBuilder keepAlive(KeepAliveOption keepAliveOption) {
        this.keepAliveOption = Preconditions.notNull(keepAliveOption);
        return this;
    }


    public <Req, Resp> OpenDingTalkStreamClientBuilder registerCallbackListener(String topic, OpenDingTalkCallbackListener<Req, Resp> callbackListener) {
        Preconditions.notNull(topic);
        Preconditions.notNull(callbackListener);
        CallbackCommandExecutor executor = (CallbackCommandExecutor) this.commands.computeIfAbsent(CommandType.CALLBACK, (key) -> new CallbackCommandExecutor());
        executor.register(topic, callbackListener);
        subscribe(CommandType.CALLBACK, topic);
        return this;
    }

    public OpenDingTalkStreamClientBuilder maxConnectionCounts(int maxConnectionCount) {
        this.maxConnectionCount = Preconditions.checkPositive(maxConnectionCount);
        return this;
    }

    public OpenDingTalkStreamClientBuilder credential(DingTalkCredential credential) {
        this.credential = Preconditions.notNull(credential);
        return this;
    }

    @Deprecated
    public OpenDingTalkStreamClientBuilder timeout(int timeout) {
        this.connectTimeout = Preconditions.checkPositive(timeout);
        return this;
    }


    public OpenDingTalkStreamClientBuilder connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }


    private OpenDingTalkStreamClientBuilder openApiHost(String host) {
        this.openApiHost = Preconditions.notNull(host);
        return this;
    }


    public OpenDingTalkStreamClientBuilder preEnv() {
        return this.openApiHost("https://pre-api.dingtalk.com");
    }

    public OpenDingTalkClient build() {
        ClientOption option = new ClientOption();
        option.setConnectTimeout(connectTimeout);
        option.setMaxConnectionCount(maxConnectionCount);
        option.setConnectionTTL(connectionTimeToLive);
        option.setOpenApiHost(openApiHost);
        option.setKeepAliveOption(keepAliveOption);
        ExecutorService executor = ThreadUtil.newFixedExecutor(consumeThreads, "DingTalk-Consumer");
        return new OpenDingTalkStreamClient(credential, new CommandDispatcher(commands), executor, option, subscriptions);
    }

    private void subscribe(CommandType type, String topic) {
        if (!subscriptions.add(new Subscription(type, topic))) {
            throw new OpenDingTalkAppException(DingTalkAppError.DUPLICATE_TOPIC_ERROR, type.name(), topic);
        }
    }
}
