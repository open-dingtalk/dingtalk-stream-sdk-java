package com.dingtalk.open.app.api.graph;

/**
 * @author feiyin
 * @date 2023/9/22
 */
public class StatusLine {

    public static final StatusLine OK = new StatusLine(200, "OK");

    public static final StatusLine NOT_FOUND = new StatusLine(404, "NOT FOUND");

    public static final StatusLine INTERNAL_ERROR = new StatusLine(500, "INTERNAL ERROR");

    private Integer code;

    private String reasonPhrase;

    public StatusLine() {

    }

    public StatusLine(Integer code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }
}
