package com.dingtalk.open.app.stream.network.ws;

import com.alibaba.fastjson2.JSON;
import com.dingtalk.open.app.stream.network.api.utils.NettyByteBufUtils;
import com.dingtalk.open.app.stream.protocol.ProtocolRequest;
import com.dingtalk.open.app.stream.protocol.ProtocolResponse;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;

/**
 * @author feiyin
 * @date 2023/3/27
 */


public class ProtocolFrameHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            //收到请求
            try {
                byte[] data = NettyByteBufUtils.getBytes(((TextWebSocketFrame) msg).content());
                ProtocolRequest request = JSON.parseObject(data, ProtocolRequest.class);
                ctx.fireChannelRead(request);
            } finally {
                ReferenceCountUtil.safeRelease(msg);
            }
            return;
        }

        if (msg instanceof PingWebSocketFrame) {
            ctx.channel().writeAndFlush(new PongWebSocketFrame(((PingWebSocketFrame) msg).content()));
            return;
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ProtocolResponse) {
            TextWebSocketFrame frame = new TextWebSocketFrame(JSON.toJSONString(msg));
            ctx.write(frame);
        } else {
            super.write(ctx, msg, promise);
        }
    }
}
