package com.dingtalk.open.app.stream.network.api;


import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author feiyin
 * @date 2023/4/13
 */
public class NetworkSharedResources {

    private static NioEventLoopGroup EVENT_LOOP_GROUP;

    static {
        EVENT_LOOP_GROUP = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
    }

    public static EventLoopGroup getNetWorkEventLoopGroup() {
        return EVENT_LOOP_GROUP;
    }


}
