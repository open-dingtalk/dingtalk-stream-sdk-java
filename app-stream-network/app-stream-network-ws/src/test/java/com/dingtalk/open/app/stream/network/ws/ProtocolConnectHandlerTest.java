package com.dingtalk.open.app.stream.network.ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import org.junit.Assert;
import org.junit.Test;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ProtocolConnectHandlerTest {

    @Test
    public void connectTimeoutClosesChannel() throws Exception {
        CompletableFuture<Channel> future = new CompletableFuture<>();
        EmbeddedChannel channel = new EmbeddedChannel(new ProtocolConnectHandler(future, 10L));
        try {
            channel.connect(new InetSocketAddress("localhost", 1234)).syncUninterruptibly();
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(1);
            while (!future.isDone() && System.nanoTime() < deadline) {
                Thread.sleep(10);
                channel.runScheduledPendingTasks();
            }
            try {
                future.get(1, TimeUnit.SECONDS);
                Assert.fail("connect future should time out");
            } catch (ExecutionException expected) {
                // expected
            }
            channel.runPendingTasks();
            Assert.assertFalse("timed-out connection must be closed", channel.isActive());
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    @Test
    public void completedHandshakeIsNotClosedByTimeout() throws Exception {
        CompletableFuture<Channel> future = new CompletableFuture<>();
        EmbeddedChannel channel = new EmbeddedChannel(new ProtocolConnectHandler(future, 10L));
        try {
            channel.connect(new InetSocketAddress("localhost", 1234)).syncUninterruptibly();
            channel.pipeline().fireUserEventTriggered(
                    WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);

            Assert.assertSame(channel, future.get(1, TimeUnit.SECONDS));
            Thread.sleep(200);
            channel.runPendingTasks();
            Assert.assertTrue("completed handshake must win over timeout", channel.isActive());
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    @Test
    public void connectionFailureCompletesFutureAndClosesChannel() throws Exception {
        CompletableFuture<Channel> future = new CompletableFuture<>();
        ChannelOutboundHandlerAdapter failingConnect = new ChannelOutboundHandlerAdapter() {
            @Override
            public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
                                SocketAddress localAddress, ChannelPromise promise) {
                promise.setFailure(new ConnectException("simulated connect failure"));
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(
                failingConnect, new ProtocolConnectHandler(future, 5000L));
        try {
            channel.connect(new InetSocketAddress("localhost", 1234));
            try {
                future.get(1, TimeUnit.SECONDS);
                Assert.fail("connect future should fail");
            } catch (ExecutionException expected) {
                Assert.assertTrue(expected.getCause() instanceof ConnectException);
            }
            channel.runPendingTasks();
            Assert.assertFalse("failed connection must be closed", channel.isActive());
        } finally {
            channel.finishAndReleaseAll();
        }
    }
}
