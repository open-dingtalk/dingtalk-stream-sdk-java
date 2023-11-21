package com.dingtalk.open.app.api.open;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.open.http.HttpConstants;
import com.dingtalk.open.app.api.util.IoUtils;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * @author feiyin
 * @date 2023/2/9
 */
class HttpOpenApiClient implements OpenApiClient {

    private final String host;

    private final int timeout;

    public HttpOpenApiClient(String host, int timeout) {
        this.host = host;
        this.timeout = timeout;
    }

    @Override
    public OpenConnectionResponse openConnection(OpenConnectionRequest request, Proxy proxy) throws Exception {
        URL url =  new URL(host + "/v1.0/gateway/connections/open");

        HttpURLConnection connection;
        if (proxy != null) {
            connection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        connection.setRequestMethod(HttpConstants.METHOD_POST);
        connection.setReadTimeout(this.timeout);
        connection.setConnectTimeout(this.timeout);
        connection.setRequestProperty(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_APPLICATION_JSON);
        connection.setRequestProperty(HttpConstants.HEADER_ACCEPT, "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        connection.getOutputStream().write(JSON.toJSONBytes(request, SerializerFeature.WriteEnumUsingToString));
        connection.getOutputStream().flush();
        if (connection.getResponseCode() == HttpConstants.STATUS_OK) {
            byte[] content = IoUtils.readAll(connection.getInputStream());
            return JSON.parseObject(content, OpenConnectionResponse.class);
        } else {
            byte[] content = IoUtils.readAll(connection.getErrorStream());
            throw DingTalkAppError.HTTP_ERROR_RESPONSE.toException(String.format("status=%s,msg=%s", connection.getResponseCode(), content != null ? new String(content) : ""));
        }
    }
}
