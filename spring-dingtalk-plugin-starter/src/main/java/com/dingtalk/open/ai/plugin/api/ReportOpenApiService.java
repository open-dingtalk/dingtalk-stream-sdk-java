package com.dingtalk.open.ai.plugin.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.open.http.HttpConstants;
import com.dingtalk.open.app.api.util.IoUtils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author feiyin
 * @date 2023/11/7
 */
public class ReportOpenApiService {

    private static final String RESOURCE = "/v1.0/gateway/plugins/report";

    private static final String HOST = "https://pre-api.dingtalk.com";


    //TODO 重构抽象一下
    public static ReportPluginResult report(ReportPluginRequest request) throws Exception {
        final HttpURLConnection connection = (HttpURLConnection) new URL(HOST + RESOURCE).openConnection();
        connection.setRequestMethod(HttpConstants.METHOD_POST);
        connection.setReadTimeout(3000);
        connection.setConnectTimeout(3000);
        connection.setRequestProperty(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_APPLICATION_JSON);
        connection.setRequestProperty(HttpConstants.HEADER_ACCEPT, "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        connection.getOutputStream().write(JSON.toJSONBytes(request, SerializerFeature.WriteEnumUsingToString));
        connection.getOutputStream().flush();
        if (connection.getResponseCode() == HttpConstants.STATUS_OK) {
            byte[] content = IoUtils.readAll(connection.getInputStream());
            return JSON.parseObject(content, ReportPluginResult.class);
        } else {
            byte[] content = IoUtils.readAll(connection.getErrorStream());
            throw DingTalkAppError.HTTP_ERROR_RESPONSE.toException(String.format("status=%s,msg=%s", connection.getResponseCode(), content != null ? new String(content) : ""));
        }
    }

}
