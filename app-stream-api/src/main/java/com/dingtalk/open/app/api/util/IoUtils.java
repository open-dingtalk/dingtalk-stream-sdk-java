package com.dingtalk.open.app.api.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author feiyin
 * @date 2023/3/1
 */
public class IoUtils {
    private static final int EOF = -1;
    private static final int BUFFER_SIZE = 4 * 1024;

    public static byte[] readAll(InputStream input) throws Exception {
        if (input == null) {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[BUFFER_SIZE];
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
