package com.zhaopeng.eagle.entity;

import com.zhaopeng.eagle.invoker.InvokerBootStrap;
import com.zhaopeng.eagle.invoker.InvokerServiceHandler;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by zhaopeng on 2016/10/30.
 */
public class InvocationServiceProxy<T> implements InvocationHandler {

    Class<T> interfaceClass;

    String url;

    public InvocationServiceProxy(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public InvocationServiceProxy(Class<T> interfaceClass,String url) {
        this.interfaceClass = interfaceClass;
        this.url=url;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 执行方法 代理, 使用netty发送 请求
        // 这里需要使用zk获取到provider的服务地址，获取到host和port
        InvokerBootStrap invokerBootStrap = new InvokerBootStrap(url);
        invokerBootStrap.connect();
        System.out.println("netty 启动");

        InvokerServiceHandler hanlder = invokerBootStrap.chooseHandler();
        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(interfaceClass.getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());


        RPCFuture future = hanlder.sendRequest(request);
        System.out.println(future.get());
        return future.get();


    }


}
