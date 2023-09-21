package com.dingtalk.open.app.api;

/**
 * @author feiyin
 * @date 2023/3/2
 */
public class Preconditions {

    public static long checkPositive(long value) {
        if (value <= 0) {
            throw DingTalkAppError.ILLEGAL_PARAM_ERROR.toException("value must greater than zero");
        }
        return value;
    }

    public static int checkPositive(int value) {
        if (value <= 0) {
            throw DingTalkAppError.ILLEGAL_PARAM_ERROR.toException("value must greater than zero");
        }
        return value;
    }

    public static <T> T notNull(T value) {
        if (value == null) {
            throw DingTalkAppError.ILLEGAL_PARAM_ERROR.toException("value can not be null");
        }
        return value;
    }
}
