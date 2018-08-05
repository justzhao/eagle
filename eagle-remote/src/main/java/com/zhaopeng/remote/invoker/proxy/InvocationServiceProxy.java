package com.zhaopeng.remote.invoker.proxy;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.remote.invoker.Invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by zhaopeng on 2018/7/22.
 */
public class InvocationServiceProxy<T> implements InvocationHandler {

    private final Class<T> interfaceClass;

    private final Url url;

    private final Invoker invoker;

    public InvocationServiceProxy(Class<T> interfaceClass, Url url) {
        this.interfaceClass = interfaceClass;
        this.url = url;
        this.invoker = new Invoker(url);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(invoker, args);
        }
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return invoker.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return invoker.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return invoker.equals(args[0]);
        }

        return invoker.invoker(interfaceClass.getName(),methodName,args);
    }
}
