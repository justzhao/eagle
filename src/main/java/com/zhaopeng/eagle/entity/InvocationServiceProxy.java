package com.zhaopeng.eagle.entity;

import com.zhaopeng.eagle.invoker.InvokerConfig;
import com.zhaopeng.eagle.invoker.InvokerServiceHandler;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by zhaopeng on 2016/10/30.
 */
public class InvocationServiceProxy<T> implements InvocationHandler {

    Class<T> interfaceClass;

    public InvocationServiceProxy(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 执行方法 代理, 使用netty发送 请求

        InvokerConfig invokerConfig =new InvokerConfig();
        invokerConfig.connect();

        InvokerServiceHandler hanlder=invokerConfig.chooseHandler();

       String q=interfaceClass.getName()+"_"+method.getName()+"_"+args+"$_";


       RPCFuture future= hanlder.sendRequest(q);

        return future.get();


    }




}
