package com.dingtalk.open.app.api.callback;

import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.OpenDingTalkAppException;
import com.dingtalk.open.app.api.common.AopUtils;
import com.dingtalk.open.app.api.common.LambdaUtils;
import com.dingtalk.open.app.api.util.ReflectUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author feiyin
 * @date 2023/3/17
 */
class CallbackDescriptor {
    private final CallbackMethod method;
    private final Type parameterType;

    private CallbackDescriptor(CallbackMethod callbackMethod, Type parameterType) {
        this.method = callbackMethod;
        this.parameterType = parameterType;
    }

    public static <Req, Resp> CallbackDescriptor build(OpenDingTalkCallbackListener<Req, Resp> callback) {
        Type target = ReflectUtils.getGenericParameterType(callback,0);
        if (target == null) {
            throw new OpenDingTalkAppException(DingTalkAppError.ILLEGAL_CALLBACK);
        }
        return new CallbackDescriptor(new SimpleMethod(callback), target);
    }

    public Type getParameterType() {
        return this.parameterType;
    }


    public CallbackMethod getMethod() {
        return this.method;
    }
}
