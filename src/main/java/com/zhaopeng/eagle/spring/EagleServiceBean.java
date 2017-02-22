package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.provider.ServiceFactory;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetAddress;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class EagleServiceBean implements ApplicationContextAware, InitializingBean {

    private String interfaceName;

    private String ref;

    private ApplicationContext applicationContext;


    private RegistryConfig registryConfig;


    public void init() {
        registryConfig = (RegistryConfig) applicationContext.getBean("registry");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        ServiceFactory.getInstance().getHandlerMap().put(interfaceName, applicationContext.getBean(ref));
        this.applicationContext = applicationContext;

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

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RegistryConfig registryConfig = (RegistryConfig) applicationContext.getBean("registry");

        // serviceRegistry = new ServiceRegistry(registryConfig.getAddress());
        String host = InetAddress.getLocalHost().getHostAddress();

        // serviceRegistry.addNode(interfaceName, host);

    }
}
