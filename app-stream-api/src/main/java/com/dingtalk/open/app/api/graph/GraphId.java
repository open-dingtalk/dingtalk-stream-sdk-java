package com.dingtalk.open.app.api.graph;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.OpenDingTalkAppException;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author feiyin
 * @date 2023/11/6
 */
public class GraphId {

    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");

    private String version;


    private GraphAPIMethod method;

    private String resource;


    public GraphId(GraphAPIMethod method, String version, String resource) {
        this.version = version;
        this.method = method;
        this.resource = resource;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public GraphAPIMethod getMethod() {
        return method;
    }

    public void setMethod(GraphAPIMethod method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GraphId graphId = (GraphId) object;
        return Objects.equals(version, graphId.version) && method == graphId.method && Objects.equals(resource, graphId.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, method, resource);
    }

    public static GraphId fromRequestLine(RequestLine requestLine) {
        final GraphAPIMethod method = requestLine.getMethod();
        String rawLine = requestLine.getUri().getPath();
        if (!rawLine.startsWith("/v")) {
            throw new OpenDingTalkAppException(DingTalkAppError.ILLEGAL_GRAPH_REQUEST_LINE, rawLine);
        }
        int resourceIndex = rawLine.indexOf("/", 2);
        String version = resourceIndex < 0 ? rawLine.substring(2) : rawLine.substring(2, resourceIndex);
        if (resourceIndex < 0) {
            return new GraphId(method, version, "/");
        }
        int rawResourceIndex = rawLine.indexOf("/", resourceIndex + 1);
        String prefix = rawResourceIndex < 0 ? rawLine.substring(resourceIndex + 1) : rawLine.substring(resourceIndex + 1, rawResourceIndex);
        if (UUID_PATTERN.matcher(prefix).find()) {
            if (rawResourceIndex < 0) {
                return new GraphId(method, version, "/");
            } else {
                return new GraphId(method, version, rawLine.substring(rawResourceIndex));
            }
        } else {
            return new GraphId(method, version, rawLine.substring(resourceIndex));
        }
    }

    public static void main(String[] args) throws Exception {
        RequestLine requestLine = new RequestLine();
        requestLine.setMethod(GraphAPIMethod.POST);
        requestLine.setUri(new URI("/v1.0/4e8413d1-4f94-4f74-bbc3-b864e605d27b"));
        System.out.println(JSON.toJSONString(fromRequestLine(requestLine)));
    }
}
