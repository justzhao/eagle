package com.zhaopeng.spring.schema;

import com.zhaopeng.spring.bean.ReferenceBean;
import com.zhaopeng.spring.bean.ServiceBean;
import com.zhaopeng.spring.bean.parser.ApplicationBeanParser;
import com.zhaopeng.spring.bean.parser.ReferenceBeanParser;
import com.zhaopeng.spring.bean.parser.ServiceBeanParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by zhaopeng on 2018/7/3.
 */
public class EagleNamespaceHandler extends NamespaceHandlerSupport {


    @Override
    public void init() {

        registerBeanDefinitionParser("application",new ApplicationBeanParser());
       // registerBeanDefinitionParser("registry",new EagleBeanDefinitionParser(RegistryConfig.class,true));
        registerBeanDefinitionParser("service", new ServiceBeanParser(ServiceBean.class));
        registerBeanDefinitionParser("reference",new ReferenceBeanParser(ReferenceBean.class));
       // registerBeanDefinitionParser("annotation", new EagleBeanDefinitionParser(AnnotationBean.class, true));
    }
}
