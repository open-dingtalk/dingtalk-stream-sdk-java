package com.dingtalk.open.app.stream.network.core;

import com.dingtalk.open.app.stream.network.api.ThreadSafe;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author feiyin
 * @date 2023/2/28
 */
@ThreadSafe
public class ExponentialBackoffPolicy implements BackoffPolicy {
    private final Random random = new Random();
    private final long initial = TimeUnit.SECONDS.toMillis(10);
    private final long max = TimeUnit.MINUTES.toMillis(1);
    private final double multiplier = 1.6;
    private final double jitter = 0.2;
    private long next = initial;

    @Override
    public long next() {
        long current = next;
        next = Math.min((long) (current * multiplier), max);
        double low = -jitter * current;
        double high = jitter * current;
        return (long) (current + random.nextDouble() * (high - low) + low);
    }
}
