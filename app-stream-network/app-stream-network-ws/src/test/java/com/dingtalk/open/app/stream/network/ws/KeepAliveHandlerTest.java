package com.dingtalk.open.app.stream.network.ws;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;

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
}
