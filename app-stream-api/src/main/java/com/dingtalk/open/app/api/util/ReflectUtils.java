package com.dingtalk.open.app.api.util;

import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.OpenDingTalkAppException;
import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import com.dingtalk.open.app.api.common.AopUtils;
import com.dingtalk.open.app.api.common.LambdaUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author feiyin
 * @date 2023/11/6
 */
public class ReflectUtils {


    public static Type getGenericParameterType(Object genericObject, int index) {
        Type target = null;
        if (LambdaUtils.isLambda(genericObject)) {
            List<Type> types = LambdaUtils.getLambdaParameterTypes(genericObject);
            if (types == null || types.size() <= index) {
                throw new OpenDingTalkAppException(DingTalkAppError.LAMBDA_PARSE_FAILED);
            }
            target = types.get(index);
        } else {
            Type[] types = AopUtils.getTargetClass(genericObject).getGenericInterfaces();
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
                    if (OpenDingTalkCallbackListener.class.isAssignableFrom(rawType)) {
                        target = ((ParameterizedType) type).getActualTypeArguments()[index];
                        break;
                    }
                }
            }
        }
        return target;
    }

}
