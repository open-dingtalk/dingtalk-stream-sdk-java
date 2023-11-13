package com.dingtalk.open.ai.plugin.util;

/**
 * @author feiyin
 * @date 2023/11/13
 */
public class Pair<First, Second> {
    private final First first;
    private final Second second;

    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    public First getFirst() {
        return first;
    }

    public Second getSecond() {
        return second;
    }
}
