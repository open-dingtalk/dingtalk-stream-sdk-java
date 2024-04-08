package com.dingtalk.open.app.api.graph;

import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import com.dingtalk.open.app.api.util.GraphUtils;
import com.dingtalk.open.app.api.util.IoUtils;
import com.dingtalk.open.app.stream.network.api.logger.InternalLogger;
import com.dingtalk.open.app.stream.network.api.logger.InternalLoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author feiyin
 * @date 2024/4/1
 */
public class GraphDispatcher implements OpenDingTalkCallbackListener<GraphAPIRequest, GraphAPIResponse> {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(GraphDispatcher.class);

    private final int port;

    public GraphDispatcher(int port) {
        this.port = port;
    }

    @Override
    public GraphAPIResponse execute(GraphAPIRequest request) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("http://127.0.0.1:" + port + request.getRequestLine().getUri().toString()).openConnection();
            connection.setRequestMethod(request.getRequestLine().getMethod().name());
            if (request.getHeaders() != null) {
                for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
            }
            connection.setDoOutput(true);
            if (request.getRequestLine().getMethod() == GraphAPIMethod.POST) {
                connection.setDoInput(true);
            }
            connection.connect();
            if (request.getRequestLine().getMethod() == GraphAPIMethod.POST) {
                if (request.getBody() != null) {
                    connection.getOutputStream().write(request.getBody().getBytes());
                }
                connection.getOutputStream().flush();
            }
            int code = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            StatusLine statusLine = new StatusLine(code, msg);
            GraphAPIResponse response = new GraphAPIResponse();
            response.setStatusLine(statusLine);
            if (code < 400) {
                response.setBody(new String(IoUtils.readAll(connection.getInputStream())));
            } else {
                response.setBody(new String(IoUtils.readAll(connection.getErrorStream())));
            }
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            Map<String, String> headers = new HashMap<>(4);
            if (headerFields != null) {
                for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null && !entry.getValue().isEmpty()) {
                        headers.put(entry.getKey().toLowerCase(), entry.getValue().get(0));
                    }
                }
            }
            response.setHeaders(headers);
            return response;
        } catch (Exception e) {
            LOGGER.error("[DingTalk] failed to forward graph request", e);
            return GraphUtils.failed(new StatusLine(500, e.getMessage()));
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
