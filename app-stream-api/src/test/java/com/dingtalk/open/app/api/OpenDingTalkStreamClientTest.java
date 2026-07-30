package com.dingtalk.open.app.api;

import com.dingtalk.open.app.api.command.CommandDispatcher;
import com.dingtalk.open.app.api.security.DingTalkCredential;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OpenDingTalkStreamClientTest {

    @Test
    public void openApiTimeoutUsesConnectTimeoutRange() {
        Assert.assertEquals(3_000, OpenDingTalkStreamClient.toHttpTimeout(3_000L));
        Assert.assertEquals(
                Integer.MAX_VALUE,
                OpenDingTalkStreamClient.toHttpTimeout((long) Integer.MAX_VALUE + 1L));
    }

    @Test(expected = OpenDingTalkAppException.class)
    public void connectTimeoutMustBePositive() {
        OpenDingTalkStreamClientBuilder.custom().connectTimeout(0);
    }

    @Test(expected = OpenDingTalkAppException.class)
    public void buildRequiresCredentialBeforeCreatingExecutor() {
        OpenDingTalkStreamClientBuilder.custom().build();
    }

    @Test
    public void stopBeforeStartTerminatesConsumerExecutor() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch taskStarted = new CountDownLatch(1);
        executor.execute(() -> {
            taskStarted.countDown();
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        });
        Assert.assertTrue(taskStarted.await(1, TimeUnit.SECONDS));

        OpenDingTalkStreamClient client = new OpenDingTalkStreamClient(
                new TestCredential(),
                new CommandDispatcher(new HashMap<>()),
                executor,
                new ClientOption(),
                Collections.emptySet(),
                null);
        client.stop();

        Assert.assertTrue(executor.isShutdown());
        Assert.assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));
        Assert.assertTrue(executor.isTerminated());
    }

    private static class TestCredential implements DingTalkCredential {
        @Override
        public String getClientId() {
            return "client-id";
        }

        @Override
        public String getClientSecret() {
            return "client-secret";
        }
    }
}
