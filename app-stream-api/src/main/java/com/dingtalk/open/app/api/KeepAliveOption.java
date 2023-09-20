package com.dingtalk.open.app.api;

/**
 * @author feiyin
 * @date 2023/9/4
 */
public class KeepAliveOption {

    /**
     * 最小时间为1s
     */
    private static final Long MINI_KEEP_ALIVE_IDLE = 1000L;
    private Long keepAliveIdle = 120 * 1000L;

    public static KeepAliveOption create() {
        return new KeepAliveOption();
    }

    protected KeepAliveOption() {

    }

    public Long getKeepAliveIdleMill() {
        return keepAliveIdle;
    }
    /**
     * 毫秒
     *
     * @param keepAliveIdle
     */
    public  KeepAliveOption withKeepAliveIdleMill(Long keepAliveIdle) {
        if (keepAliveIdle < MINI_KEEP_ALIVE_IDLE) {
            throw new OpenDingTalkAppException(DingTalkAppError.ILLEGAL_KEEP_ALIVE_IDLE);
        }
        this.keepAliveIdle = keepAliveIdle;
        return this;
    }
}
