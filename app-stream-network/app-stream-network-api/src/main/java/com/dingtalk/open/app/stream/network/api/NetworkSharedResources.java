package com.dingtalk.open.app.stream.network.api;


import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author feiyin
 * @date 2023/4/13
 */
public class NetworkSharedResources {

    private static NioEventLoopGroup EVENT_LOOP_GROUP;
    private static int referenceCount;

    public static synchronized EventLoopGroup acquireNetWorkEventLoopGroup() {
        referenceCount++;
        return getNetWorkEventLoopGroup();
    }

    public static synchronized EventLoopGroup getNetWorkEventLoopGroup() {
        if (EVENT_LOOP_GROUP == null
                || EVENT_LOOP_GROUP.isShuttingDown()
                || EVENT_LOOP_GROUP.isShutdown()
                || EVENT_LOOP_GROUP.isTerminated()) {
            EVENT_LOOP_GROUP = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        }
        return EVENT_LOOP_GROUP;
    }

    public static synchronized void releaseNetWorkEventLoopGroup() {
        if (referenceCount == 0) {
            return;
        }
        referenceCount--;
        if (referenceCount == 0 && EVENT_LOOP_GROUP != null) {
            EVENT_LOOP_GROUP.shutdownGracefully();
            EVENT_LOOP_GROUP = null;
        }
    }
}
