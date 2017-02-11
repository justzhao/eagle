package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.provider.ServiceFactory;
import com.zhaopeng.eagle.registry.ServiceRegistry;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class EagleServiceBean implements ApplicationContextAware, InitializingBean {

    private String interfaceName;

    private String ref;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        ServiceFactory.getInstance().getHandlerMap().put(interfaceName, applicationContext.getBean(ref));
        this.applicationContext = applicationContext;
        // 在这里设置好对应的service方法到 map中


    }


    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RegistryConfig registryConfig = (RegistryConfig) applicationContext.getBean("registry");
        //注册服务
        ServiceRegistry serviceRegistry= registryConfig.getServiceRegistry();

        serviceRegistry.addNode(interfaceName,"127.0.0.1");

    }
}
