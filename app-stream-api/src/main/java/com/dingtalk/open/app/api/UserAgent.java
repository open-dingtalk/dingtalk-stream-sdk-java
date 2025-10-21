package com.dingtalk.open.app.api;

/**
 * @author feiyin
 * @date 2023/3/29
 */
public class UserAgent {

    private final String name = "java";
    private String version;

    private UserAgent() {

    }

    public static UserAgent getUserAgent() {
        UserAgent userAgent = new UserAgent();
        userAgent.version = UserAgent.class.getPackage().getImplementationVersion();
        return userAgent;
    }

    public String getUa() {
        return this.name + "/" + this.version + "-union";
    }

}
