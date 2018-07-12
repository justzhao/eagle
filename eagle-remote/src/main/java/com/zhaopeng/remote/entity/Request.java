package com.zhaopeng.remote.entity;

/**
 * Created by zhaopeng on 2016/11/4.
 */
public class Request {

    // 请求id
    private String requestId;
    // 类名字
    private String className;
    // 方法名字
    private String methodName;
    // 参数类型
    private Class<?>[] parameterTypes;
    // 参数
    private Object[] parameters;

    public  boolean heartEvent=false;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public boolean isHeartEvent() {
        return heartEvent;
    }

    public void setHeartEvent(boolean heartEvent) {
        this.heartEvent = heartEvent;
    }
}
