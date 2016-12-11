package com.zhaopeng.eagle.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by zhaopeng on 2016/11/16.
 */
public class CommonNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("application",new EagleApplicationBeanParser());
        registerBeanDefinitionParser("service", new EagleServiceBeanParser());
        registerBeanDefinitionParser("registry",new EagleBeanDefinitionParser(RegistryConfig.class,true));

    }
}
