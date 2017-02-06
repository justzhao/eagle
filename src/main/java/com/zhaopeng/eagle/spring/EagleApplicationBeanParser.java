package com.zhaopeng.eagle.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by zhaopeng on 2016/11/19.
 */
public class EagleApplicationBeanParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String port = element.getAttribute("port");
        String protocol = element.getAttribute("protocol");
        String threads = element.getAttribute("threads");
        String accepts = element.getAttribute("accepts");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(EagleApplicationBean.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.setInitMethodName("init");
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("threads", threads);
        beanDefinition.getPropertyValues().addPropertyValue("accepts", accepts);
        parserContext.getRegistry().registerBeanDefinition("eagle", beanDefinition);
        return beanDefinition;

    }
}
