package com.dingtalk.open.app.stream.protocol;

import java.util.Map;

/**
 * @author feiyin
 * @date 2023/4/7
 */
public class DefaultProtocolRequestFacade implements ProtocolRequestFacade {
    private final ProtocolRequest request;

    public DefaultProtocolRequestFacade(ProtocolRequest request) {
        this.request = request;
    }

    @Override
    public String getMessageId() {
        return getHeader(HeaderNames.MESSAGE_ID);
    }

    @Override
    public String getContentType() {
        return getHeader(HeaderNames.CONTENT_TYPE);
    }
    @Override
    public String getTopic() {
        return getHeader(HeaderNames.TOPIC);
    }
    @Override
    public CommandType getType() {
        return request.getType();
    }
    @Override
    public String getData() {
        return request.getData();
    }
    @Override
    public String getHeader(String headerName) {
        if (headerName == null) {
            return null;
        }
        Map<String, String> headers = request.getHeaders();
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        return headers.get(headerName);
    }
    @Override
    public ProtocolRequest getRequest() {
        return this.request;
    }
}
