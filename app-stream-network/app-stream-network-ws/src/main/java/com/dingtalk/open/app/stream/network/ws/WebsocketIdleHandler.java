package com.dingtalk.open.app.stream.network.ws;

import io.netty.handler.timeout.IdleStateHandler;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author feiyin
 * @date 2023/6/13
 */
public class WebsocketIdleHandler extends IdleStateHandler {

    public WebsocketIdleHandler(Duration keepAliveIdle) {
        super(keepAliveIdle.toMillis(), keepAliveIdle.toMillis(), keepAliveIdle.toMillis(), TimeUnit.MILLISECONDS);
    }
}
