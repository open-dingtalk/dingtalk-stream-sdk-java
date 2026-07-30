package com.dingtalk.open.app.stream.network.api.exception;

import org.junit.Assert;
import org.junit.Test;

public class DingTalkNetworkExceptionTest {

    @Test
    public void preservesNetworkErrorAndCause() {
        IllegalStateException cause = new IllegalStateException("service loader failed");
        DingTalkNetworkException exception =
                new DingTalkNetworkException(NetWorkError.OPEN_CONNECTION_ERROR, cause);

        Assert.assertEquals(NetWorkError.OPEN_CONNECTION_ERROR, exception.getNetWorkError());
        Assert.assertEquals("OPEN_CONNECTION_ERROR", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
    }

    @Test
    public void includesProtocolDetail() {
        DingTalkNetworkException exception =
                new DingTalkNetworkException(NetWorkError.PROTOCOL_ILLEGAL, "protocol WSS");

        Assert.assertEquals(NetWorkError.PROTOCOL_ILLEGAL, exception.getNetWorkError());
        Assert.assertEquals("PROTOCOL_ILLEGAL: protocol WSS", exception.getMessage());
    }
}
