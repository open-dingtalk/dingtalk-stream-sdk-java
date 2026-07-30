package com.dingtalk.open.app.stream.network.api;

import io.netty.channel.EventLoopGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class NetworkSharedResourcesTest {

    @Test
    public void sharedEventLoopStopsAfterLastRelease() throws Exception {
        EventLoopGroup first = NetworkSharedResources.acquireNetWorkEventLoopGroup();
        EventLoopGroup second = NetworkSharedResources.acquireNetWorkEventLoopGroup();
        Assert.assertSame(first, second);
        first.submit(() -> {
            // Start a real event-loop thread; an unstarted group can appear to
            // shut down correctly without proving that process-exit threads end.
        }).syncUninterruptibly();

        NetworkSharedResources.releaseNetWorkEventLoopGroup();
        Assert.assertFalse("one active client still owns the event loop", first.isShuttingDown());

        NetworkSharedResources.releaseNetWorkEventLoopGroup();
        Assert.assertTrue("last client release must stop the event loop", first.isShuttingDown());
        Assert.assertTrue(
                "started event-loop threads must terminate after the last client releases them",
                first.awaitTermination(10, TimeUnit.SECONDS));
        Assert.assertTrue(first.isTerminated());

        EventLoopGroup replacement = NetworkSharedResources.acquireNetWorkEventLoopGroup();
        try {
            Assert.assertNotSame("a stopped event loop must not be reused", first, replacement);
        } finally {
            NetworkSharedResources.releaseNetWorkEventLoopGroup();
        }
    }
}
