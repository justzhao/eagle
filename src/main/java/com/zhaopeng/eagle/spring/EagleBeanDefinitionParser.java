package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.spring.registry.EagleReferenceBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by zhaopeng on 2016/12/11.
 */
public class EagleBeanDefinitionParser implements BeanDefinitionParser {

    private final Class<?> beanClass;

    private final boolean required;



    public EagleBeanDefinitionParser(Class<?> beanClass, boolean required) {
        this.beanClass = beanClass;
        this.required = required;
    }
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return parse(element, parserContext, beanClass, required);
    }


    public BeanDefinition parse(Element element, ParserContext parserContext,Class<?> beanClass, boolean required) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        if(RegistryConfig.class.equals(beanClass)){
            String address=element.getAttribute("address");
            beanDefinition.setInitMethodName("init");
            beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
            beanDefinition.getPropertyValues().add("address",address);
            parserContext.getRegistry().registerBeanDefinition("registry", beanDefinition);
        }else if(EagleReferenceBean.class.equals(beanClass)){
            beanDefinition.setInitMethodName("init");
            String interfaceName = element.getAttribute("interface");
            String id=element.getAttribute("id");
            beanDefinition.getPropertyValues().add("interfaceName",interfaceName);
            parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        }
        return beanDefinition;
    }
}
