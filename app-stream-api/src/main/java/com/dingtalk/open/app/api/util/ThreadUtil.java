package com.dingtalk.open.app.api.util;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author feiyin
 * @date 2023/3/3
 */
public class ThreadUtil {

    private static final int QUEUE_SIZE = 2048;

    private static final int DEFAULT_KEEP_LIVE = 3000;

    public static ExecutorService newFixedExecutor(int count, String name) {
        return new ThreadPoolExecutor(count, count, DEFAULT_KEEP_LIVE, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(QUEUE_SIZE), new DefaultThreadFactory(name));

    }
}
