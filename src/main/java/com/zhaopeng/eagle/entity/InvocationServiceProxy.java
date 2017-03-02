package com.zhaopeng.eagle.entity;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.invoker.InvokerBootStrap;
import com.zhaopeng.eagle.invoker.InvokerServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by zhaopeng on 2016/10/30.
 */
public class InvocationServiceProxy<T> implements InvocationHandler {
    private final static Logger logger = LoggerFactory.getLogger(InvocationServiceProxy.class);
    Class<T> interfaceClass;

    URL url;




    public InvocationServiceProxy(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public InvocationServiceProxy(Class<T> interfaceClass, String url) {
        this.interfaceClass = interfaceClass;

    }

    public InvocationServiceProxy(URL url, Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
        this.url=url;


    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(interfaceClass.getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        return invoke(request);
    }

    public Object invoke(Request request) throws Throwable {

        int retries =url.getParameter(Constants.RETRIES,3);
        InvokerBootStrap invokerBootStrap = new InvokerBootStrap(url);
        invokerBootStrap.connect();
        InvokerServiceHandler handler = invokerBootStrap.chooseHandler();
        while (retries > 0) {
            RPCFuture future = handler.sendRequest(request);
            if (future.get() != null) {
                return future.get();
            }
            retries--;
        }
        return null;
    }

}
