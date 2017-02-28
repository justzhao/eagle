package com.zhaopeng.eagle.entity;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.invoker.InvokerBootStrap;
import com.zhaopeng.eagle.invoker.InvokerServiceHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhaopeng on 2016/10/30.
 */
public class InvocationServiceProxy<T> implements InvocationHandler {

    Class<T> interfaceClass;

    URL url;


    //String url;

    List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public InvocationServiceProxy(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public InvocationServiceProxy(Class<T> interfaceClass, String url) {
        this.interfaceClass = interfaceClass;
        //   this.url = url;
    }

    public InvocationServiceProxy(URL url, Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
        urls = url.getUrls();

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
        if(urls==null||urls.isEmpty()){
            return null;
        }
        String str = urls.get(0);
        InvokerBootStrap invokerBootStrap = new InvokerBootStrap(str);
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
