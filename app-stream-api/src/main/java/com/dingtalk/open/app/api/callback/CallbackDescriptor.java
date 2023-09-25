package com.dingtalk.open.app.api.callback;

import com.dingtalk.open.app.api.DingTalkAppError;
import com.dingtalk.open.app.api.OpenDingTalkAppException;
import com.dingtalk.open.app.api.common.AopUtils;
import com.dingtalk.open.app.api.common.LambdaUtils;
import com.dingtalk.open.app.api.stream.ServerStreamCallbackListener;

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
    private final CallbackType callbackType;

    private CallbackDescriptor(CallbackMethod callbackMethod, Type parameterType, CallbackType callbackType) {
        this.method = callbackMethod;
        this.parameterType = parameterType;
        this.callbackType = callbackType;
    }

    public static <Req, Resp> CallbackDescriptor build(OpenDingTalkCallbackListener<Req, Resp> callback) {
        Type target = null;
        if (LambdaUtils.isLambda(callback)) {
            List<Type> types = LambdaUtils.getLambdaParameterTypes(callback);
            if (types == null || types.size() != 1) {
                throw new OpenDingTalkAppException(DingTalkAppError.LAMBDA_PARSE_FAILED);
            }
            target = types.get(0);
        } else {
            Type[] types = AopUtils.getTargetClass(callback).getGenericInterfaces();
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
                    if (OpenDingTalkCallbackListener.class.isAssignableFrom(rawType)) {
                        target = ((ParameterizedType) type).getActualTypeArguments()[0];
                        break;
                    }
                }
            }
        }
        if (target == null) {
            throw new OpenDingTalkAppException(DingTalkAppError.ILLEGAL_CALLBACK);
        }
        return new CallbackDescriptor(new UnaryMethod(callback), target, CallbackType.SERVER_STREAM);
    }

    public static <Req, Resp> CallbackDescriptor build(ServerStreamCallbackListener<Req, Resp> callback) {
        Type target = null;
        if (LambdaUtils.isLambda(callback)) {
            List<Type> types = LambdaUtils.getLambdaParameterTypes(callback);
            if (types == null || types.size() != 2) {
                throw new OpenDingTalkAppException(DingTalkAppError.LAMBDA_PARSE_FAILED);
            }
            target = types.get(0);
        } else {
            Type[] types = AopUtils.getTargetClass(callback).getGenericInterfaces();
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
                    if (ServerStreamCallbackListener.class.isAssignableFrom(rawType)) {
                        target = ((ParameterizedType) type).getActualTypeArguments()[0];
                        break;
                    }
                }
            }
        }
        if (target == null) {
            throw new OpenDingTalkAppException(DingTalkAppError.ILLEGAL_CALLBACK);
        }
        return new CallbackDescriptor(new ServerStreamCallMethod(callback), target, CallbackType.SERVER_STREAM);
    }

    public Type getParameterType() {
        return this.parameterType;
    }


    public CallbackMethod getMethod() {
        return this.method;
    }

    public CallbackType getCallbackType() {
        return callbackType;
    }
}
