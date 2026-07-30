package com.dingtalk.open.app.stream.network.api;

import io.netty.channel.EventLoopGroup;
import org.junit.Assert;
import org.junit.Test;

public class NetworkSharedResourcesTest {

    @Test
    public void sharedEventLoopStopsAfterLastRelease() {
        EventLoopGroup first = NetworkSharedResources.acquireNetWorkEventLoopGroup();
        EventLoopGroup second = NetworkSharedResources.acquireNetWorkEventLoopGroup();
        Assert.assertSame(first, second);

        NetworkSharedResources.releaseNetWorkEventLoopGroup();
        Assert.assertFalse("one active client still owns the event loop", first.isShuttingDown());

        NetworkSharedResources.releaseNetWorkEventLoopGroup();
        Assert.assertTrue("last client release must stop the event loop", first.isShuttingDown());

        EventLoopGroup replacement = NetworkSharedResources.acquireNetWorkEventLoopGroup();
        try {
            Assert.assertNotSame("a stopped event loop must not be reused", first, replacement);
        } finally {
            NetworkSharedResources.releaseNetWorkEventLoopGroup();
        }
    }
}
