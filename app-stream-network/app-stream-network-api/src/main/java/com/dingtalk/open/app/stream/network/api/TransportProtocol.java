package com.dingtalk.open.app.stream.network.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feiyin
 * @date 2023/3/24
 */
public enum TransportProtocol {
    /**
     * websocket协议
     */
    WSS("wss", true),
    /**
     * rsocket_tcp协议
     */
    RSOCKET_TCP("rsocket", true);

    private String scheme;

    private boolean tls;

    TransportProtocol(String scheme, boolean tls) {
        this.scheme = scheme;
        this.tls = tls;
    }

    public String getScheme() {
        return scheme;
    }

    public boolean isTls() {
        return this.tls;
    }


    public static TransportProtocol parseScheme(String scheme) {
        if (scheme == null) {
            return null;
        }
        return MAPPING.get(scheme);
    }

    private final static Map<String, TransportProtocol> MAPPING = new HashMap<>();

    static {
        for (TransportProtocol value : TransportProtocol.values()) {
            MAPPING.put(value.scheme, value);
        }
    }


}
