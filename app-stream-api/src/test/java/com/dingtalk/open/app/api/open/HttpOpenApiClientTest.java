package com.dingtalk.open.app.api.open;

import com.dingtalk.open.app.stream.network.api.NetProxy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class HttpOpenApiClientTest {

    private static final String PROXY_HOST = "127.0.0.1";
    private static final int PROXY_PORT = 3128;

    @Before
    public void clearDefaultAuthenticatorBeforeTest() {
        Authenticator.setDefault(null);
    }

    @After
    public void clearDefaultAuthenticatorAfterTest() {
        Authenticator.setDefault(null);
    }

    @Test
    public void credentialsAreScopedToMatchingProxyRequests() throws Exception {
        NetProxy proxy = new NetProxy(PROXY_HOST, PROXY_PORT, "proxy-user", "proxy-password");

        HttpOpenApiClient.withProxyAuthentication(proxy, () -> {
            PasswordAuthentication matching = requestAuthentication(
                    PROXY_HOST,
                    InetAddress.getByName(PROXY_HOST),
                    PROXY_PORT,
                    Authenticator.RequestorType.PROXY);
            Assert.assertNotNull(matching);
            Assert.assertEquals("proxy-user", matching.getUserName());
            Assert.assertArrayEquals("proxy-password".toCharArray(), matching.getPassword());

            Assert.assertNull(requestAuthentication(
                    PROXY_HOST,
                    InetAddress.getByName(PROXY_HOST),
                    PROXY_PORT,
                    Authenticator.RequestorType.SERVER));
            Assert.assertNull(requestAuthentication(
                    "127.0.0.2",
                    InetAddress.getByName("127.0.0.2"),
                    PROXY_PORT,
                    Authenticator.RequestorType.PROXY));
            Assert.assertNull(requestAuthentication(
                    PROXY_HOST,
                    InetAddress.getByName(PROXY_HOST),
                    PROXY_PORT + 1,
                    Authenticator.RequestorType.PROXY));
            return null;
        });

        Assert.assertNull(requestAuthentication(
                PROXY_HOST,
                InetAddress.getByName(PROXY_HOST),
                PROXY_PORT,
                Authenticator.RequestorType.PROXY));
    }

    @Test
    public void credentialsAreClearedWhenRequestFails() throws Exception {
        NetProxy proxy = new NetProxy(PROXY_HOST, PROXY_PORT, "proxy-user", "proxy-password");
        Exception expected = new Exception("simulated request failure");

        try {
            HttpOpenApiClient.withProxyAuthentication(proxy, () -> {
                throw expected;
            });
            Assert.fail("request failure should propagate");
        } catch (Exception actual) {
            Assert.assertSame(expected, actual);
        }

        Assert.assertNull(requestAuthentication(
                PROXY_HOST,
                InetAddress.getByName(PROXY_HOST),
                PROXY_PORT,
                Authenticator.RequestorType.PROXY));
    }

    @Test
    public void hostAuthenticatorIsRestoredAfterProxyRequest() throws Exception {
        Authenticator hostAuthenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "host-user",
                        "host-password".toCharArray());
            }
        };
        Authenticator.setDefault(hostAuthenticator);
        NetProxy proxy = new NetProxy(PROXY_HOST, PROXY_PORT, "proxy-user", "proxy-password");

        HttpOpenApiClient.withProxyAuthentication(proxy, () -> {
            PasswordAuthentication proxyAuthentication = requestAuthentication(
                    PROXY_HOST,
                    InetAddress.getByName(PROXY_HOST),
                    PROXY_PORT,
                    Authenticator.RequestorType.PROXY);
            Assert.assertNotNull(proxyAuthentication);
            Assert.assertEquals("proxy-user", proxyAuthentication.getUserName());
            return null;
        });

        PasswordAuthentication restored = requestAuthentication(
                "service.example",
                InetAddress.getByName(PROXY_HOST),
                443,
                Authenticator.RequestorType.SERVER);
        Assert.assertNotNull(restored);
        Assert.assertEquals("host-user", restored.getUserName());
        Assert.assertArrayEquals(
                "host-password".toCharArray(),
                restored.getPassword());
    }

    @Test
    public void concurrentClientsCannotObserveEachOthersProxyCredentials() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Future<Void>> futures = new ArrayList<>();
        try {
            for (int index = 0; index < 100; index++) {
                final String username = "proxy-user-" + index;
                final String password = "proxy-password-" + index;
                futures.add(executor.submit(() -> HttpOpenApiClient.withProxyAuthentication(
                        new NetProxy(PROXY_HOST, PROXY_PORT, username, password),
                        () -> {
                            PasswordAuthentication authentication = requestAuthentication(
                                    PROXY_HOST,
                                    InetAddress.getByName(PROXY_HOST),
                                    PROXY_PORT,
                                    Authenticator.RequestorType.PROXY);
                            Assert.assertNotNull(authentication);
                            Assert.assertEquals(username, authentication.getUserName());
                            Assert.assertArrayEquals(
                                    password.toCharArray(),
                                    authentication.getPassword());
                            return null;
                        })));
            }
            for (Future<Void> future : futures) {
                future.get(5, TimeUnit.SECONDS);
            }
        } finally {
            executor.shutdownNow();
            Assert.assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    private static PasswordAuthentication requestAuthentication(
            String host,
            InetAddress address,
            int port,
            Authenticator.RequestorType requestorType) throws Exception {
        return Authenticator.requestPasswordAuthentication(
                host,
                address,
                port,
                "https",
                "proxy authentication",
                "basic",
                new URL("https://api.dingtalk.com/v1.0/gateway/connections/open"),
                requestorType);
    }
}
