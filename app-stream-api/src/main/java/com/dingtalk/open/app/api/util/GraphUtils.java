package com.dingtalk.open.app.api.util;

import com.alibaba.fastjson.JSON;
import com.dingtalk.open.app.api.graph.GraphAPIResponse;
import com.dingtalk.open.app.api.graph.GraphHeaders;
import com.dingtalk.open.app.api.graph.MediaType;
import com.dingtalk.open.app.api.graph.StatusLine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class GraphUtils {

    /**
     * 返回成功json
     *
     * @param result
     * @return
     */
    public static GraphAPIResponse successJson(Object result) {
        return success(JSON.toJSONString(result));
    }


    public static GraphAPIResponse success(String result) {
        GraphAPIResponse response = baseResponse(StatusLine.OK);
        response.setBody(result);
        return response;
    }

    public static GraphAPIResponse failed(StatusLine statusLine) {
        return baseResponse(statusLine);
    }

    private static GraphAPIResponse baseResponse(StatusLine statusLine) {
        Map<String, String> headers = new HashMap<>();
        headers.put(GraphHeaders.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_VALUE);
        GraphAPIResponse response = new GraphAPIResponse();
        response.setHeaders(headers);
        response.setStatusLine(statusLine);
        return response;
    }
}
