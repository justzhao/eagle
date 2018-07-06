package com.zhaopeng.spring.bean.parser;

import com.google.common.base.Strings;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by zhaopeng on 2018/7/6.
 */
public class RegisterBeanParser extends AbstractBeanParser implements BeanDefinitionParser {


    public RegisterBeanParser(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        String address = element.getAttribute("address");
        if (Strings.isNullOrEmpty(address)) {
            throw new IllegalStateException("Register address must set");
        }
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.getPropertyValues().add("address", address);
        return beanDefinition;
    }
}
