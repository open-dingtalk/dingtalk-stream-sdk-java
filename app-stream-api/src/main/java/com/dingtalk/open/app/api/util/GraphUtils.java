package com.dingtalk.open.app.api.util;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.api.models.graph.GraphAPIResponse;
import com.dingtalk.open.app.api.models.graph.GraphHeaders;
import com.dingtalk.open.app.api.models.graph.MediaType;
import com.dingtalk.open.app.api.models.graph.StatusLine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class GraphUtils {

    public static GraphAPIResponse successJson(Object result) {
        GraphAPIResponse response = baseResponse(StatusLine.OK);
        response.setBody(JSON.toJSONString(result));
        return response;
    }

    public static GraphAPIResponse failed(StatusLine statusLine) {
        GraphAPIResponse response = baseResponse(statusLine);
        return response;
    }

    private static GraphAPIResponse baseResponse(StatusLine statusLine) {
        Map<String, String> headers = new HashMap<>();
        headers.put(GraphHeaders.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_VALUE);
        GraphAPIResponse response = new GraphAPIResponse();
        response.setHeaders(headers);
        response.setStatusLine(statusLine);
        return response;
    }

    private static GraphAPIResponse baseResponse(int code, String reasonPhrase) {
        final StatusLine statusLine = new StatusLine();
        statusLine.setCode(code);
        statusLine.setReasonPhrase(reasonPhrase);
        return baseResponse(statusLine);
    }
}
