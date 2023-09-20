package com.dingtalk.open.app.api.util;

import java.io.InputStream;

/**
 * @author feiyin
 * @date 2023/3/1
 */
public class IoUtils {
    public static byte[] readAll(InputStream stream) throws Exception {
        if (stream == null) {
            return null;
        }
        int count = stream.available();
        byte[] content = new byte[count];
        while (count > 0) {
            int readBytes = stream.read(content);
            count -= readBytes;
        }
        return content;
    }
}
