package com.zhaopeng.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public class ServiceBean<T> implements ApplicationContextAware, InitializingBean {

    /**
     * 接口路径
     */
    private String interfaceName;

    /**
     * 引用的对象
     */
    private T ref;

    /**
     * spring上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        checkConfig();
        register();
        export();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void checkConfig(){

    }
    private void register(){

    }
    private void  export(){

    }
}
