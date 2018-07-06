package com.zhaopeng.spring.bean;

import com.zhaopeng.spring.config.AbstractConfig;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public class ReferenceBean extends AbstractConfig implements FactoryBean {



    @Override
    public Object getObject() throws Exception {

        /**
         * 这里返回代理
         */

        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
