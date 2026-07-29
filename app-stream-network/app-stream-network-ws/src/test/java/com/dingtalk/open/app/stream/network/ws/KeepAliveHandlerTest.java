package com.dingtalk.open.app.stream.network.ws;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Regression for Aone 84622857 / customer logs:
 * IdleStateEvent before WS handshake must not NPE in KeepAliveHandler.
 */
public class KeepAliveHandlerTest {

    @Test
    public void idleBeforeHandshakeDoesNotThrow() {
        KeepAliveHandler handler = new KeepAliveHandler(Duration.ofSeconds(5));
        EmbeddedChannel channel = new EmbeddedChannel(handler);
        try {
            // Multiple idle events before handshake — reproduces Aone 84622857 NPE path.
            channel.pipeline().fireUserEventTriggered(IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT);
            channel.pipeline().fireUserEventTriggered(IdleStateEvent.READER_IDLE_STATE_EVENT);
            channel.pipeline().fireUserEventTriggered(IdleStateEvent.WRITER_IDLE_STATE_EVENT);
            Assert.assertTrue(channel.isActive());
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    @Test
    public void idleAfterHandshakeSendsPing() {
        KeepAliveHandler handler = new KeepAliveHandler(Duration.ofSeconds(5));
        EmbeddedChannel channel = new EmbeddedChannel(handler);
        Object outbound = null;
        try {
            channel.pipeline().fireUserEventTriggered(
                    WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
            channel.pipeline().fireUserEventTriggered(IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT);
            channel.runPendingTasks();
            outbound = channel.readOutbound();
            Assert.assertTrue(outbound instanceof PingWebSocketFrame);
            Assert.assertNull(channel.readOutbound());
            Assert.assertTrue(channel.isActive());
        } finally {
            ReferenceCountUtil.release(outbound);
            channel.finishAndReleaseAll();
        }
    }

    @Test
    public void multipleIdleEventsQueueOnlyOnePingWhileWriteIsPending() {
        DelayedWriteHandler delayedWrite = new DelayedWriteHandler();
        KeepAliveHandler handler = new KeepAliveHandler(Duration.ofSeconds(5));
        EmbeddedChannel channel = new EmbeddedChannel(delayedWrite, handler);
        try {
            channel.pipeline().fireUserEventTriggered(
                    WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
            channel.pipeline().fireUserEventTriggered(IdleStateEvent.READER_IDLE_STATE_EVENT);
            channel.pipeline().fireUserEventTriggered(IdleStateEvent.WRITER_IDLE_STATE_EVENT);
            channel.pipeline().fireUserEventTriggered(IdleStateEvent.ALL_IDLE_STATE_EVENT);
            channel.runPendingTasks();

            Assert.assertEquals("only one ping may be in flight", 1, delayedWrite.writes.get());
        } finally {
            delayedWrite.failPending();
            channel.finishAndReleaseAll();
        }
    }

    @Test
    public void pendingPingWriteStillTimesOut() throws Exception {
        DelayedWriteHandler delayedWrite = new DelayedWriteHandler();
        KeepAliveHandler handler = new KeepAliveHandler(Duration.ofMillis(10));
        EmbeddedChannel channel = new EmbeddedChannel(delayedWrite, handler);
        try {
            channel.pipeline().fireUserEventTriggered(
                    WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
            channel.pipeline().fireUserEventTriggered(IdleStateEvent.READER_IDLE_STATE_EVENT);
            channel.runPendingTasks();

            long deadline = System.nanoTime() + Duration.ofSeconds(2).toNanos();
            while (channel.isActive() && System.nanoTime() < deadline) {
                Thread.sleep(10);
                channel.runPendingTasks();
            }
            Assert.assertFalse("pending ping write must be bounded by keepAlive timeout", channel.isActive());
        } finally {
            delayedWrite.failPending();
            channel.finishAndReleaseAll();
        }
    }

    private static class DelayedWriteHandler extends ChannelOutboundHandlerAdapter {
        private final AtomicInteger writes = new AtomicInteger();
        private final List<ChannelPromise> promises = new ArrayList<>();

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            writes.incrementAndGet();
            ReferenceCountUtil.release(msg);
            promises.add(promise);
        }

        private void failPending() {
            for (ChannelPromise promise : promises) {
                promise.tryFailure(new IOException("test cleanup"));
            }
        }
    }
}
