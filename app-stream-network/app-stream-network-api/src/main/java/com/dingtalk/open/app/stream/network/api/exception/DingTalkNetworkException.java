package com.dingtalk.open.app.stream.network.api.exception;

/**
 * @author feiyin
 * @date 2023/1/5
 */
public class DingTalkNetworkException extends RuntimeException {

    private final NetWorkError netWorkError;

    public DingTalkNetworkException(NetWorkError netWorkError) {
        super(netWorkError != null ? netWorkError.name() : null);
        this.netWorkError = netWorkError;
    }

    public DingTalkNetworkException(NetWorkError netWorkError, Throwable cause) {
        super(netWorkError != null ? netWorkError.name() : null, cause);
        this.netWorkError = netWorkError;
    }

    public DingTalkNetworkException(NetWorkError netWorkError, String detail) {
        super(netWorkError != null ? netWorkError.name() + ": " + detail : detail);
        this.netWorkError = netWorkError;
    }

    public NetWorkError getNetWorkError() {
        return netWorkError;
    }

}
