package com.zhaopeng.spring.bean.parser;

import com.google.common.base.Strings;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public class ReferenceBeanParser extends AbstractBeanParser implements BeanDefinitionParser {

    public ReferenceBeanParser(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition beanDefinition=new RootBeanDefinition();

        beanDefinition.setLazyInit(false);
        beanDefinition.setBeanClass(beanClass);

        String id = element.getAttribute("id");
        String interfaceName = element.getAttribute("interface");
        if (Strings.isNullOrEmpty(id)) {
            id = interfaceName;
        }

        if(Strings.isNullOrEmpty(id)){
            throw new IllegalStateException("reference interface must set");
        }

        String timeout=element.getAttribute("timeout");
        String retries=element.getAttribute("retries");

        beanDefinition.getPropertyValues().addPropertyValue("id", id);
        beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);
        beanDefinition.getPropertyValues().addPropertyValue("retries", retries);
        beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceName);
        return beanDefinition;
    }
}
