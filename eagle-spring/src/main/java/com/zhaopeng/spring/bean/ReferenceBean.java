package com.zhaopeng.spring.bean;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.remote.invoker.proxy.ProxyServiceFactory;
import com.zhaopeng.spring.config.AbstractConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public class ReferenceBean<T> extends AbstractConfig implements ApplicationContextAware, FactoryBean, InitializingBean {


    private transient volatile T ref;

    private ApplicationContext applicationContext;


    @Override
    public Object getObject() throws Exception {

        /**
         * 这里返回代理
         */
        if (ref == null) {
            init();
        }

        return ref;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    private void init() {

        Url url = new Url();
        ref = ProxyServiceFactory.newServiceInstance(url);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        init();
    }
}
