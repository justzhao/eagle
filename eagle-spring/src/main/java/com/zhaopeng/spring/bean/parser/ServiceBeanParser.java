package com.zhaopeng.spring.bean.parser;

import com.google.common.base.Strings;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public class ServiceBeanParser extends AbstractBeanParser implements BeanDefinitionParser {

    public ServiceBeanParser(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        String id = element.getAttribute("id");
        String interfaceName = element.getAttribute("interfaceName");
        if (Strings.isNullOrEmpty(interfaceName)) {
            throw new IllegalStateException("the interfaceName must set");
        }
        beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceName);
        if (Strings.isNullOrEmpty(id)) {
            id = interfaceName;
        }
        beanDefinition.getPropertyValues().addPropertyValue("id", id);
        String ref = element.getAttribute("ref");
        BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(ref);
        if (!refBean.isSingleton()) {
            throw new IllegalStateException("The exported service ref " + ref + " must be singleton! Please set the " + ref + " bean scope to singleton, eg: <bean id=\"" + ref + "\" scope=\"singleton\" ...>");
        }
        Object reference = new RuntimeBeanReference(ref);
        beanDefinition.getPropertyValues().addPropertyValue("ref", reference);

        return beanDefinition;
    }
}
