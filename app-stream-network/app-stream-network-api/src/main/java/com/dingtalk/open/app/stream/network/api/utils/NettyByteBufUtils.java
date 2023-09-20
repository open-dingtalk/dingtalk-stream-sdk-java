package com.dingtalk.open.app.stream.network.api.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.PlatformDependent;

import java.util.Arrays;

import static io.netty.util.internal.MathUtil.isOutOfBounds;

/**
 * @author feiyin
 * @date 2023/4/12
 */
public class NettyByteBufUtils {
    public static byte[] getBytes(ByteBuf buf) {
        return getBytes(buf, buf.readerIndex(), buf.readableBytes(), true);
    }
    public static byte[] getBytes(ByteBuf buf, int start, int length, boolean copy) {
        int capacity = buf.capacity();
        if (isOutOfBounds(start, length, capacity)) {
            throw new IndexOutOfBoundsException("expected: " + "0 <= start(" + start + ") <= start + length(" + length + ") <= " + "buf.capacity(" + capacity + ')');
        }

        if (buf.hasArray()) {
            int baseOffset = buf.arrayOffset() + start;
            byte[] bytes = buf.array();
            if (copy || baseOffset != 0 || length != bytes.length) {
                return Arrays.copyOfRange(bytes, baseOffset, baseOffset + length);
            } else {
                return bytes;
            }
        }

        byte[] bytes = PlatformDependent.allocateUninitializedArray(length);
        buf.getBytes(start, bytes);
        return bytes;
    }
}
