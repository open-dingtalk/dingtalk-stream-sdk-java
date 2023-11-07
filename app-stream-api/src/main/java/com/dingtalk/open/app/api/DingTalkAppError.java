package com.dingtalk.open.app.api;

/**
 * @author feiyin
 * @date 2023/3/2
 */
public enum DingTalkAppError {

    /**
     * 异常信息
     */
    ILLEGAL_PARAM_ERROR(400, "10000", "illegal param"),

    ILLEGAL_URL(400, "10001", "illegal url"),

    HTTP_ERROR_RESPONSE(500, "10002", "http failed status"),

    ILLEGAL_PROTOCOL(400, "10003", "illegal protocol"),
    /**
     * 未知content-type
     */
    UNKNOWN_CONTENT_TYPE(400, "10004", "unknown content type"),


    BAD_REQUEST_TYPE(400, "10005", "bad request data type"),


    ILLEGAL_CALLBACK(400, "10006", "illegal callback implementation"),


    ILLEGAL_GRAPH_REQUEST_LINE(400, "10007", "illegal graph request line"),


    ILLEGAL_GENERIC_LISTENER(400, "10008", "illegal generic listener"),


    DUPLICATE_DEFINED_GRAPH(400, "10009", "duplicated defined graph api"),


    DUPLICATE_DEFINED_CALLBACK(400, "10010", "duplicated callback registered"),

    REFLECTION_ERROR(500, "10008", " reflection error"),


    TOPIC_NOT_EXIST(404, "10009", "topic not exist"),

    TYPE_NOT_EXIST(404, "100010", "type not exist"),

    DUPLICATE_TOPIC_ERROR(500, "10011", "duplicate topic"),

    LAMBDA_PARSE_FAILED(500, "10012", "not valid lambda"),

    ILLEGAL_KEEP_ALIVE_IDLE(400, "10013", "keep alive idle can not be less than 1000 millisecond"),


    CLIENT_STATE_ERROR(500, "10014", "client state error"),


    /**
     * 未知错误
     */
    UNKNOWN_ERROR(500, "99999", "unknown error");

    private final int code;

    private final String errorCode;
    private final String message;

    DingTalkAppError(int code, String errorCode, String message) {
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }


    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public OpenDingTalkAppException toException(String... appendMessage) {
        return new OpenDingTalkAppException(this, appendMessage);
    }
}
