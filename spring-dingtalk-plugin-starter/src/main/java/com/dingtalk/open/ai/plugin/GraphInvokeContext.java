package com.dingtalk.open.ai.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author feiyin
 * @date 2023/11/15
 */
public class GraphInvokeContext {

    private static final ThreadLocal<GraphInvokeContext> CONTEXT = ThreadLocal.withInitial(GraphInvokeContext::new);

    private Map<String, String> headers;

    public void addHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(name, value);
    }

    public void addAllHeaders(Map<String, String> allHeaders) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (allHeaders == null) {
            return;
        }
        headers.putAll(allHeaders);
    }

    /**
     * 获取名称
     *
     * @param name
     * @return
     */
    public String getHeader(String name) {
        if (headers == null || name == null) {
            return null;
        } else {
            return headers.get(name);
        }
    }


    public static void start(GraphInvokeContext context) {
        CONTEXT.set(context);
    }

    public static void clear() {
        CONTEXT.remove();
    }


}
