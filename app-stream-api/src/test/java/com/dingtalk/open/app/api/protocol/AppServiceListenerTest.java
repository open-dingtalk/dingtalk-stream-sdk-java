package com.dingtalk.open.app.api.protocol;

import com.dingtalk.open.app.api.command.CommandDispatcher;
import com.dingtalk.open.app.stream.network.api.Context;
import com.dingtalk.open.app.stream.protocol.CommandType;
import com.dingtalk.open.app.stream.protocol.ProtocolRequest;
import com.dingtalk.open.app.stream.protocol.ProtocolRequestFacade;
import com.dingtalk.open.app.stream.protocol.event.AckPayload;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class AppServiceListenerTest {

    private static final Executor REJECTING_EXECUTOR = command -> {
        throw new RejectedExecutionException("full");
    };

    @Test
    public void overloadedEventIsAcknowledgedForLaterRetry() {
        TestContext context = new TestContext(CommandType.EVENT, "event-topic", "{}");
        AppServiceListener listener = new AppServiceListener(
                new CommandDispatcher(new HashMap<>()),
                REJECTING_EXECUTOR);

        listener.receive(context);

        Assert.assertTrue(context.response instanceof AckPayload);
        Assert.assertEquals(EventAckStatus.LATER, ((AckPayload) context.response).getStatus());
        Assert.assertNull(context.failure);
    }

    @Test
    public void systemCommandBypassesSaturatedConsumerExecutor() {
        TestContext context = new TestContext(CommandType.SYSTEM, "ping", "{}");
        AppServiceListener listener = new AppServiceListener(
                new CommandDispatcher(new HashMap<>()),
                REJECTING_EXECUTOR);

        listener.receive(context);

        Assert.assertNotNull(context.response);
        Assert.assertNull(context.failure);
    }

    private static class TestContext implements Context {
        private final ProtocolRequestFacade request;
        private Object response;
        private Throwable failure;

        private TestContext(CommandType type, String topic, String data) {
            this.request = new TestRequest(type, topic, data);
        }

        @Override
        public void replay(Object payload) {
            response = payload;
        }

        @Override
        public void exception(Throwable t) {
            failure = t;
        }

        @Override
        public String connectionId() {
            return "test-connection";
        }

        @Override
        public ProtocolRequestFacade getRequest() {
            return request;
        }
    }

    private static class TestRequest implements ProtocolRequestFacade {
        private final CommandType type;
        private final String topic;
        private final String data;

        private TestRequest(CommandType type, String topic, String data) {
            this.type = type;
            this.topic = topic;
            this.data = data;
        }

        @Override
        public String getMessageId() {
            return "test-message";
        }

        @Override
        public String getContentType() {
            return "application/json";
        }

        @Override
        public String getTopic() {
            return topic;
        }

        @Override
        public CommandType getType() {
            return type;
        }

        @Override
        public String getData() {
            return data;
        }

        @Override
        public String getHeader(String headerName) {
            return null;
        }

        @Override
        public ProtocolRequest getRequest() {
            return null;
        }
    }
}
