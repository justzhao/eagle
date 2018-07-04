package com.zhaopeng.spring.bean.parser;

import org.springframework.beans.factory.config.BeanDefinition;
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
        return null;
    }
}
