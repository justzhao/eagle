package com.zhaopeng.spring.bean.parser;

import com.zhaopeng.spring.bean.ApplicationBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public class ApplicationBeanParser implements BeanDefinitionParser {


    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        String port = element.getAttribute("port");
        String protocol = element.getAttribute("protocol");
        String threads = element.getAttribute("threads");
        String accepts = element.getAttribute("accepts");
        String registerUrl = element.getAttribute("registerUrl");
        String applicationName = element.getAttribute("applicationName");
        beanDefinition.setBeanClass(ApplicationBean.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("threads", threads);
        beanDefinition.getPropertyValues().addPropertyValue("accepts", accepts);
        beanDefinition.getPropertyValues().addPropertyValue("registerUrl", registerUrl);
        beanDefinition.getPropertyValues().addPropertyValue("applicationName", applicationName);
        parserContext.getRegistry().registerBeanDefinition("eagle", beanDefinition);
        return beanDefinition;
    }
}
