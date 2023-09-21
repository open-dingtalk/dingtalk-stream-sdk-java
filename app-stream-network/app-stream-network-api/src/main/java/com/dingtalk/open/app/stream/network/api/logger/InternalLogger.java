package com.dingtalk.open.app.stream.network.api.logger;

/**
 * @author feiyin
 * @date 2022/12/28
 */
public interface InternalLogger {

    /**
     * info日志
     *
     * @param format
     * @param args
     */
    void info(String format, Object... args);


    /**
     * warn
     *
     * @param format
     * @param args
     */
    void warn(String format, Object... args);

    /**
     * 错误日志
     *
     * @param format
     * @param e
     * @param args
     */
    void error(String format, Exception e, Object... args);

    /**
     * 错误日志
     *
     * @param format
     * @param args
     */
    void error(String format, Object... args);

}
