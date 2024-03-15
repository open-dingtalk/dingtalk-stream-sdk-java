package com.dingtalk.open.app.api.util;

/**
 * @author feiyin
 * @date 2024/3/15
 */
public class JavaVersionUtil {


    public static Integer getMainVersion() {
        String getVersion = System.getProperty("java.version");
        String[] version = getVersion.split("\\.");
        if (version[0].equalsIgnoreCase("1")) {
            return Integer.parseInt(version[1]);
        } else {
            return Integer.parseInt(version[0]);
        }
    }
}
