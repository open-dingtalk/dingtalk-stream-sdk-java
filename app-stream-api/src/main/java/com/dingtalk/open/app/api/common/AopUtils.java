package com.dingtalk.open.app.api.common;

import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.OpenDingTalkAppException;

import java.lang.reflect.Method;

/**
 * @author feiyin
 * @date 2023/3/17
 */
public class AopUtils {

    private static final String SPRING_PROXY = "org.springframework.aop.SpringProxy";

    private static final String TARGET_CLASS_AWARE = "org.springframework.aop.TargetClassAware";

    private static final String DECORATING_PROXY = "org.springframework.core.DecoratingProxy";

    public static Class<?> getTargetClass(Object candidate) {
        Class<?> result = null;
        if (instanceOf(candidate.getClass(), TARGET_CLASS_AWARE)) {
            result = (Class<?>) invoke(candidate, TARGET_CLASS_AWARE, "getTargetClass");
        }

        if (result == null) {
            if (isCglibProxy(candidate)) {
                result = candidate.getClass().getSuperclass();
            } else if (isDecoratingProxy(candidate)) {
                result = (Class<?>) invoke(candidate, DECORATING_PROXY, "getDecoratedClass");
            } else {
                result = candidate.getClass();
            }
        }

        return result;
    }

    public static boolean isCglibProxy(Object object) {
        return instanceOf(object.getClass(), SPRING_PROXY) && object.getClass().getCanonicalName().contains("$$");
    }

    private static boolean isDecoratingProxy(Object candidate) {
        return instanceOf(candidate.getClass(), DECORATING_PROXY);
    }


    private static boolean instanceOf(Class clazz, String interfaceName) {
        Class<?>[] superInterfaces = clazz.getInterfaces();
        for (Class<?> interfaceClass : superInterfaces) {
            if (interfaceClass.getCanonicalName().equals(interfaceName)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static Object invoke(Object object, String clazzName, String methodName, Object... args) {
        try {
            Class<?> klass = Class.forName(clazzName);
            Method method = klass.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new OpenDingTalkAppException(DingTalkAppError.REFLECTION_ERROR);
        }
    }
}
