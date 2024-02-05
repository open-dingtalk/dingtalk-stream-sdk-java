package com.dingtalk.open.app.api.chatbot;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.dingtalk.open.app.api.open.http.HttpConstants;
import com.dingtalk.open.app.api.util.IoUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson2.JSONWriter.Feature.WriteEnumUsingToString;

public class BotReplier {
    private final String webhook;
    private final int timeout = 60000;

    public BotReplier(String webhook) {
        this.webhook = webhook;
    }

    public static BotReplier fromWebhook(String webhook) {
        return new BotReplier(webhook);
    }

    public String replyText(String text) throws IOException {
        return replyText(text, null);
    }
    public String replyMarkdown(String title, String text) throws IOException {
        return replyMarkdown(title, text, null);
    }

    public String replyText(String text, List<String> atUserIds) throws IOException {
        final HttpURLConnection connection = getHttpURLConnection();

        Map<String, Object> textContent = new HashMap<>();
        textContent.put("content", text);
        Map<String, Object> request = new HashMap<>();
        request.put("msgtype", "text");
        request.put("text", textContent);
        if (atUserIds != null && !atUserIds.isEmpty()) {
            Map<String, Object> atContent = new HashMap<>();
            atContent.put("atUserIds", Collections.singletonList(""));
            request.put("at", atContent);
        }
        connection.getOutputStream().write(JSON.toJSONBytes(request, WriteEnumUsingToString));
        connection.getOutputStream().flush();
        if (connection.getResponseCode() == HttpConstants.STATUS_OK) {
            try {
                byte[] bytes = IoUtils.readAll(connection.getInputStream());
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        } else {
            try {
                byte[] bytes = IoUtils.readAll(connection.getErrorStream());
                throw new IOException(String.format("status=%s, msg=%s", connection.getResponseCode(), bytes != null ? new String(bytes) : ""));
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
    }


    public String replyMarkdown(String title, String text, List<String> atUserIds) throws IOException {
        final HttpURLConnection connection = getHttpURLConnection();
        Map<String, Object> markdownContent = new HashMap<>();
        markdownContent.put("title", title);
        markdownContent.put("text", text);
        Map<String, Object> request = new HashMap<>();
        request.put("msgtype", "markdown");
        request.put("markdown", markdownContent);
        if (atUserIds != null && !atUserIds.isEmpty()) {
            Map<String, Object> atContent = new HashMap<>();
            atContent.put("atUserIds", Collections.singletonList(""));
            request.put("at", atContent);
        }
        connection.getOutputStream().write(JSON.toJSONBytes(request, WriteEnumUsingToString));
        connection.getOutputStream().flush();
        if (connection.getResponseCode() == HttpConstants.STATUS_OK) {
            try {
                byte[] bytes = IoUtils.readAll(connection.getInputStream());
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        } else {
            try {
                byte[] bytes = IoUtils.readAll(connection.getErrorStream());
                throw new IOException(String.format("status=%s, msg=%s", connection.getResponseCode(), bytes != null ? new String(bytes) : ""));
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(webhook).openConnection();
        connection.setRequestMethod(HttpConstants.METHOD_POST);
        connection.setReadTimeout(this.timeout);
        connection.setConnectTimeout(this.timeout);
        connection.setRequestProperty(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_APPLICATION_JSON);
        connection.setRequestProperty(HttpConstants.HEADER_ACCEPT, "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        return connection;
    }
}
