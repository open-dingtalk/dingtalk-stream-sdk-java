package com.dingtalk.open.app.stream.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feiyin
 * @date 2022/12/28
 */
public class ProtocolResponse {
    private int code;
    private String message;

    private Map<String, String> headers;
    private String data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHeader(String headerName, String headerValue) {
        if (headerName == null || headerValue == null) {
            return;
        }
        if (headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(headerName, headerValue);
    }

    public static ProtocolResponse new200Response(ProtocolRequestFacade request, boolean endOfStream) {
        ProtocolResponse response = base(request);
        response.setCode(200);
        response.setMessage("OK");
        response.setHeader(HeaderNames.MESSAGE_ID, request.getMessageId());
        response.setHeader(HeaderNames.CONTENT_TYPE, request.getContentType());
        response.setHeader(HeaderNames.TIME, String.valueOf(System.currentTimeMillis()));
        response.setHeader(HeaderNames.STREAM_STAGE, endOfStream ? StreamConstants.STREAM_STAGE_END : StreamConstants.STREAM_STAGE_DATA);
        return response;
    }

    public static ProtocolResponse new500Response(ProtocolRequestFacade request, String message, boolean endOfStream) {
        ProtocolResponse response = base(request);
        response.setCode(500);
        response.setMessage("client internal error:" + message);
        response.setHeader(HeaderNames.STREAM_STAGE, endOfStream ? StreamConstants.STREAM_STAGE_END : StreamConstants.STREAM_STAGE_DATA);
        return response;
    }


    public static ProtocolResponse newErrorResponse(ProtocolRequestFacade request, int code, String msg, boolean endOfStream) {
        ProtocolResponse response = base(request);
        response.setCode(code);
        response.setMessage(msg);
        response.setHeader(HeaderNames.STREAM_STAGE, endOfStream ? StreamConstants.STREAM_STAGE_END : StreamConstants.STREAM_STAGE_DATA);
        return response;
    }

    private static ProtocolResponse base(ProtocolRequestFacade request) {
        ProtocolResponse response = new ProtocolResponse();
        response.setHeader(HeaderNames.MESSAGE_ID, request.getMessageId());
        response.setHeader(HeaderNames.CONTENT_TYPE, ContentType.APPLICATION_JSON.getContentType());
        return response;
    }
}
