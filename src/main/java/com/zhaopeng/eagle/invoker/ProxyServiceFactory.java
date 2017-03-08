package com.zhaopeng.eagle.invoker;


import com.zhaopeng.eagle.entity.InvocationServiceProxy;
import com.zhaopeng.eagle.entity.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * 生成不同的代理类
 * Created by zhaopeng on 2016/10/29.
 */
public class ProxyServiceFactory {
    private final static Logger logger = LoggerFactory.getLogger(ProxyServiceFactory.class);


    @SuppressWarnings("unchecked")
    public static <T> T newServiceInstance(Class<T> interfaceClass) throws IllegalAccessException {
        if (interfaceClass == null) {
            throw new IllegalAccessException("Interface class == null");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalAccessException(interfaceClass.getName() + " must be interface");
        }
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationServiceProxy(interfaceClass));

    }

    public static <T> T newServiceInstance(Class<T> interfaceClass, String url) throws IllegalAccessException {
        if (interfaceClass == null) {
            throw new IllegalAccessException("Interface class == null");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalAccessException(interfaceClass.getName() + " must be interface");
        }
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationServiceProxy(interfaceClass, url));

    }

    public static <T> T newServiceInstance(URL url)  {

        try {
            Class<?> interfaceClass = Class.forName(url.getInterfaceName());
            if (interfaceClass == null) {
                throw new IllegalAccessException("Interface class == null");
            }
            if (!interfaceClass.isInterface()) {
                throw new IllegalAccessException(interfaceClass.getName() + " must be interface");
            }
            return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationServiceProxy(url,interfaceClass));

        }catch (Exception e){

            logger.error("create client proxy fail {}",e);
        }

        return null;

    }
}
