package com.dingtalk.open.app.api.common;

import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.OpenDingTalkAppException;
import com.dingtalk.open.app.api.util.JavaVersionUtil;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author feiyin
 * @date 2023/3/19
 */
public class LambdaUtils {
    private static final Pattern LAMBDA_PATTERN_JDK8_20 = Pattern.compile(".*\\$\\$Lambda\\$[0-9]+/.*");

    private static final Pattern LAMBDA_PATTERN_JDK_21 = Pattern.compile(".*\\$\\$Lambda/.*");

    private static final Pattern PARAMETER_TYPE_PATTERN = Pattern.compile("\\((.*)\\).*");

    private static final String WRITE_REPLACE = "writeReplace";

    public static boolean isLambda(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass().isSynthetic()) {
            if (JavaVersionUtil.getMainVersion() < 21) {
                return LAMBDA_PATTERN_JDK8_20.matcher(obj.getClass().getSimpleName()).matches();
            } else {
                return LAMBDA_PATTERN_JDK_21.matcher(obj.getClass().getSimpleName()).matches();
            }
        }
        return false;
    }


    public static List<Type> getLambdaParameterTypes(Object obj) {
        try {
            Method method = obj.getClass().getDeclaredMethod(WRITE_REPLACE);
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(obj);
            Matcher matcher = PARAMETER_TYPE_PATTERN.matcher(lambda.getInstantiatedMethodType());
            if (!matcher.find() || matcher.groupCount() != 1) {
                throw new OpenDingTalkAppException(DingTalkAppError.LAMBDA_PARSE_FAILED);
            }
            return Arrays.stream(matcher.group(1).split(";")).filter(s -> !s.isEmpty()).map(s -> s.replace("L", "").replace("/", ".")).map(s -> {
                try {
                    return Class.forName(s);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("can not load class", e);
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
