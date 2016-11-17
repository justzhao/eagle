package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.provider.config.ServiceFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by zhaopeng on 2016/11/16.
 */
public class EagleServiceBeanParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String interfaceName = element.getAttribute("interfaceName");
        String ref = element.getAttribute("ref");
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(EagleServiceBean.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceName);
        beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
        parserContext.getRegistry().registerBeanDefinition(interfaceName, beanDefinition);

        ServiceFactory.serverStart();

        return beanDefinition;
    }
}
