package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.annotation.AnnotationBean;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by zhaopeng on 2016/11/16.
 */
public class CommonNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("application",new EagleApplicationBeanParser());
        registerBeanDefinitionParser("registry",new EagleBeanDefinitionParser(RegistryConfig.class,true));
        registerBeanDefinitionParser("service", new EagleServiceBeanParser());
        registerBeanDefinitionParser("reference",new EagleBeanDefinitionParser(EagleReferenceBean.class,true));
        registerBeanDefinitionParser("annotation", new EagleBeanDefinitionParser(AnnotationBean.class, true));
    }
}
