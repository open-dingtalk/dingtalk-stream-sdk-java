package com.dingtalk.open.ai.plugin;

/**
 * @author feiyin
 * @date 2023/11/29
 */
public class Env {

    private static final String HOST = "https://api.dingtalk.com";

    private static final String PRE_HOST = "https://pre-api.dingtalk.com";

    private static volatile Boolean PRE = false;


    public static void setPre() {
        PRE = true;
    }

    public static String getOpenAPIHost() {
        if (PRE) {
            return PRE_HOST;
        } else {
            return HOST;
        }
    }

    public static Boolean isPre() {
        return PRE;
    }
}
