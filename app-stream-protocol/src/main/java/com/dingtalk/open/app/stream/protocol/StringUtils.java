package com.dingtalk.open.app.stream.protocol;

/**
 * @author feiyin
 * @date 2023/3/7
 */
public class StringUtils {
    public static boolean isDigital(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i< s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String cs) {
        return cs == null || cs.isEmpty();
    }

    public static boolean isNotEmpty(String cs) {
        return !isEmpty(cs);
    }
}
