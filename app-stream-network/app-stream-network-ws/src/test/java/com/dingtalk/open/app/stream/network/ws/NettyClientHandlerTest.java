package com.dingtalk.open.app.stream.network.ws;

import com.dingtalk.open.app.stream.network.api.ClientConnectionListener;
import com.dingtalk.open.app.stream.network.api.Context;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ensure exceptionCaught closes the channel so session pool can reconnect.
 */
public class NettyClientHandlerTest {

    @Test
    public void exceptionCaughtClosesActiveChannel() {
        assertExceptionClosesChannel(new RuntimeException("simulated handler failure"));
    }

    @Test
    public void connectionResetClosesActiveChannel() {
        assertExceptionClosesChannel(new IOException("Connection reset by peer"));
    }

    private void assertExceptionClosesChannel(Throwable failure) {
        final AtomicBoolean disconnected = new AtomicBoolean(false);
        NettyClientHandler handler = new NettyClientHandler("conn-test", new ClientConnectionListener() {
            @Override
            public void receive(Context context) {
            }

            @Override
            public void onDisConnection(String connectionId) {
                disconnected.set(true);
            }
        });

        EmbeddedChannel channel = new EmbeddedChannel(handler);
        Assert.assertTrue(channel.isActive());
        channel.pipeline().fireExceptionCaught(failure);

        // EmbeddedChannel closes synchronously on close()
        Assert.assertFalse("channel should be closed after exceptionCaught", channel.isActive());
        // channelInactive should notify listener
        Assert.assertTrue("onDisConnection should be triggered", disconnected.get());
        channel.finishAndReleaseAll();
    }
}
