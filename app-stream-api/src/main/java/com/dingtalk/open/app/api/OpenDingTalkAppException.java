package com.dingtalk.open.app.api;

import com.dingtalk.open.app.stream.network.api.ServiceException;

/**
 * @author feiyin
 * @date 2023/3/2
 */
public class OpenDingTalkAppException extends ServiceException {
    private final int code;
    private final String errorCode;
    private final String message;
    public OpenDingTalkAppException(DingTalkAppError dingTalkAppError, String... appendMessage) {
        this.code = dingTalkAppError.getCode();
        this.errorCode = dingTalkAppError.getErrorCode();
        this.message = (appendMessage != null && appendMessage.length > 0) ? dingTalkAppError.getMessage() + appendMessage[0] : dingTalkAppError.getMessage();
    }


    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
