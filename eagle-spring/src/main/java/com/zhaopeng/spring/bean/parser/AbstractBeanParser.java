package com.zhaopeng.spring.bean.parser;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public abstract class AbstractBeanParser {

   protected final Class<?> beanClass;

    protected AbstractBeanParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}
