package com.zhaopeng.remote.invoker.proxy;

import com.zhaopeng.common.bean.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * Created by zhaopeng on 2018/7/22.
 */
public class ProxyServiceFactory {


    private static Logger logger = LoggerFactory.getLogger(ProxyServiceFactory.class);


    public static <T> T newServiceInstance(Url url) {

        try {
            Class<?> interfaceClass = Class.forName(url.getInterfaceName());
            if (interfaceClass == null) {
                throw new IllegalAccessException("Interface class == null");
            }
            if (!interfaceClass.isInterface()) {
                throw new IllegalAccessException(interfaceClass.getName() + " must be interface");
            }
            return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationServiceProxy(interfaceClass, url));
        } catch (Exception e) {
            logger.error("create client proxy fail {}", e);
        }

        return null;

    }

}
