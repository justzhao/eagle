package com.zhaopeng.spring.bean;

import com.google.common.base.Strings;
import com.zhaopeng.spring.config.AbstractConfig;
import com.zhaopeng.spring.holder.ServiceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public class ServiceBean<T> extends AbstractConfig implements ApplicationContextAware, InitializingBean {

    Logger logger = LoggerFactory.getLogger(ServiceBean.class);


    /**
     * 接口路径
     */
    private String interfaceName;

    /**
     * 引用的对象
     */
    private T ref;

    private Class<?> interfaceClass;

    /**
     * spring上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {

        register();
        export();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void checkConfig() {


    }

    private void register() {

        ApplicationBean applicationBean = applicationContext.getBean(ApplicationBean.class);

        if(Strings.isNullOrEmpty(applicationBean.getRegisterUrl())){
          throw  new IllegalStateException("<application:service registerUrl=\"\" /> registerUrl not allow null!");
        }

        /**
         * 调用注册
         */



    }

    private void export() {

        if (Strings.isNullOrEmpty(interfaceName)) {
            throw new IllegalStateException("<dubbo:service interface=\"\" /> interface not allow null!");
        }
        try {
            interfaceClass = Class.forName(interfaceName, true, Thread.currentThread()
                    .getContextClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("export class {} error ", e);
        }
        if (!interfaceClass.isInstance(ref)) {
            throw new IllegalStateException("The class "
                    + ref.getClass().getName() + " unimplemented interface "
                    + interfaceClass + "!");
        }

        ServiceHolder.putService(interfaceName, ref);

    }
}
