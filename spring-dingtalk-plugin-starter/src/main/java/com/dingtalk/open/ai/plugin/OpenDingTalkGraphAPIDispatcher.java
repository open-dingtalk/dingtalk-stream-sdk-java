package com.dingtalk.open.ai.plugin;

import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.OpenDingTalkAppException;
import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import com.dingtalk.open.app.api.graph.*;
import com.dingtalk.open.app.api.util.GraphUtils;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class OpenDingTalkGraphAPIDispatcher implements OpenDingTalkCallbackListener<GraphAPIRequest, GraphAPIResponse> {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(OpenDingTalkGraphAPIDispatcher.class);

    private final Map<GraphId, GraphMethodDescriptor> listeners;

    public OpenDingTalkGraphAPIDispatcher() {
        this.listeners = new ConcurrentHashMap<>();
    }


    @Override
    public GraphAPIResponse execute(GraphAPIRequest request) {
        GraphInvokeContext context = new GraphInvokeContext();
        context.addAllHeaders(request.getHeaders());
        GraphInvokeContext.start(context);
        try {
            GraphId graphId = GraphId.fromRequestLine(request.getRequestLine());
            GraphMethodDescriptor descriptor = listeners.get(graphId);
            if (descriptor == null) {
                //GraphAPI不存在
                return GraphUtils.failed(StatusLine.NOT_FOUND);
            }
            return GraphUtils.success(descriptor.invoke(request.getBody()));
        } catch (Exception e) {
            LOGGER.error("[DingTalk] unexpected execute exception occurs when invoke graph api", e);
            return GraphUtils.failed(StatusLine.INTERNAL_ERROR);
        } finally {
            GraphInvokeContext.clear();
        }
    }


    public void register(GraphAPIMethod method, String version, String resource, GraphMethodDescriptor descriptor) {
        if (listeners.putIfAbsent(new GraphId(method, version, resource), descriptor) != null) {
            throw new OpenDingTalkAppException(DingTalkAppError.DUPLICATE_DEFINED_GRAPH, method + " /v" + version + resource);
        }
    }
}
