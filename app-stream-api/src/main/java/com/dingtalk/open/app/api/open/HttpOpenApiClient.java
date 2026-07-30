package com.dingtalk.open.app.api.open;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.open.http.HttpConstants;
import com.dingtalk.open.app.api.util.IoUtils;
import com.dingtalk.open.app.stream.network.api.NetProxy;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author feiyin
 * @date 2023/2/9
 */
class HttpOpenApiClient implements OpenApiClient {

    /*
     * Java 8 HttpURLConnection only supports authenticated HTTPS proxies via
     * the JVM-wide Authenticator. Keep the unavoidable global hook inert
     * outside one SDK request, match the exact proxy endpoint, and serialize
     * authenticated proxy requests so credentials from different clients can
     * never overwrite one another.
     */
    private static final Object PROXY_AUTHENTICATION_LOCK = new Object();
    private static volatile ProxyAuthenticationContext activeProxyAuthentication;
    private static final Authenticator PROXY_AUTHENTICATOR = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            ProxyAuthenticationContext authentication = activeProxyAuthentication;
            if (getRequestorType() != RequestorType.PROXY
                    || authentication == null
                    || !authentication.matches(
                    getRequestingHost(),
                    getRequestingSite(),
                    getRequestingPort())) {
                return null;
            }
            return authentication.passwordAuthentication();
        }
    };

    private final String host;

    private final NetProxy netProxy;

    private final int timeout;

    public HttpOpenApiClient(String host, int timeout, NetProxy netProxy) {
        this.host = host;
        this.timeout = timeout;
        this.netProxy = netProxy;
    }

    @Override
    public OpenConnectionResponse openConnection(OpenConnectionRequest request) throws Exception {
        URL url = new URL(host + "/v1.0/gateway/connections/open");
        if (hasProxyCredentials(netProxy)) {
            return withProxyAuthentication(
                    netProxy,
                    () -> executeOpenConnection(url, request));
        }
        return executeOpenConnection(url, request);
    }

    private OpenConnectionResponse executeOpenConnection(
            URL url,
            OpenConnectionRequest request) throws Exception {
        HttpURLConnection connection;
        if (netProxy != null) {
            connection = (HttpURLConnection) url.openConnection(netProxy.getProxy());
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        try {
            connection.setRequestMethod(HttpConstants.METHOD_POST);
            connection.setReadTimeout(this.timeout);
            connection.setConnectTimeout(this.timeout);
            connection.setRequestProperty(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.CONTENT_TYPE_APPLICATION_JSON);
            connection.setRequestProperty(HttpConstants.HEADER_ACCEPT, "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            try (OutputStream output = connection.getOutputStream()) {
                output.write(JSON.toJSONBytes(request, JSONWriter.Feature.WriteEnumUsingToString));
                output.flush();
            }

            int status = connection.getResponseCode();
            InputStream responseStream = status == HttpConstants.STATUS_OK
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            byte[] content;
            if (responseStream == null) {
                content = null;
            } else {
                try (InputStream input = responseStream) {
                    content = IoUtils.readAll(input);
                }
            }

            if (status == HttpConstants.STATUS_OK) {
                return JSON.parseObject(content, OpenConnectionResponse.class);
            }
            throw DingTalkAppError.HTTP_ERROR_RESPONSE.toException(String.format(
                    "status=%s,msg=%s",
                    status,
                    content != null ? new String(content, StandardCharsets.UTF_8) : ""));
        } finally {
            connection.disconnect();
        }
    }

    static <T> T withProxyAuthentication(
            NetProxy netProxy,
            Callable<T> operation) throws Exception {
        if (!hasProxyCredentials(netProxy)) {
            return operation.call();
        }
        synchronized (PROXY_AUTHENTICATION_LOCK) {
            ProxyAuthenticationContext authentication =
                    new ProxyAuthenticationContext(netProxy);
            Authenticator previousAuthenticator = getDefaultAuthenticator();
            activeProxyAuthentication = authentication;
            Authenticator.setDefault(PROXY_AUTHENTICATOR);
            try {
                return operation.call();
            } finally {
                activeProxyAuthentication = null;
                authentication.clear();
                Authenticator.setDefault(previousAuthenticator);
            }
        }
    }

    private static Authenticator getDefaultAuthenticator() {
        try {
            // Authenticator.getDefault() is public starting with Java 9.
            Method getDefault = Authenticator.class.getMethod("getDefault");
            return (Authenticator) getDefault.invoke(null);
        } catch (NoSuchMethodException ignored) {
            try {
                // Java 8 has no public accessor. Read the same private field
                // used by setDefault so the host application's authenticator
                // can still be restored after this short request scope.
                Field field = Authenticator.class.getDeclaredField("theAuthenticator");
                field.setAccessible(true);
                return (Authenticator) field.get(null);
            } catch (ReflectiveOperationException | SecurityException e) {
                throw new IllegalStateException(
                        "cannot preserve the JVM default Authenticator", e);
            }
        } catch (ReflectiveOperationException | SecurityException e) {
            throw new IllegalStateException(
                    "cannot read the JVM default Authenticator", e);
        }
    }

    private static boolean hasProxyCredentials(NetProxy netProxy) {
        return netProxy != null
                && netProxy.getUsername() != null
                && netProxy.getPassword() != null;
    }

    private static class ProxyAuthenticationContext {
        private final String host;
        private final InetAddress address;
        private final int port;
        private final String username;
        private final char[] password;

        private ProxyAuthenticationContext(NetProxy netProxy) {
            Proxy proxy = netProxy.getProxy();
            InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
            this.host = socketAddress.getHostString();
            this.address = socketAddress.getAddress();
            this.port = socketAddress.getPort();
            this.username = netProxy.getUsername();
            this.password = netProxy.getPassword().toCharArray();
        }

        private boolean matches(
                String requestingHost,
                InetAddress requestingAddress,
                int requestingPort) {
            if (requestingPort != port) {
                return false;
            }
            if (requestingHost != null && host.equalsIgnoreCase(requestingHost)) {
                return true;
            }
            return address != null && address.equals(requestingAddress);
        }

        private PasswordAuthentication passwordAuthentication() {
            return new PasswordAuthentication(username, password);
        }

        private void clear() {
            Arrays.fill(password, '\0');
        }
    }

}
