package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.invoker.ProxyServiceFactory;
import com.zhaopeng.eagle.registry.ServiceDiscovery;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhaopeng on 2016/12/19.
 */
public class EagleReferenceBean implements FactoryBean , ApplicationContextAware {

    private String interfaceName;

    private int timeout = 200;

    private int retries = 1;

    private String url;

    private Object obj;

    private Class<?> objType;

    private ApplicationContext applicationContext;
    public String getInterfaceName() {
        return interfaceName;
    }
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public int getRetries() {
        return retries;
    }
    public void setRetries(int retries) {
        this.retries = retries;
    }
    public Object getObj() {
        return obj;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setObj(Object obj) {
        this.obj = obj;
    }
    public Class<?> getObjType() {
        return objType;
    }
    public void setObjType(Class<?> objType) {
        this.objType = objType;
    }
    @Override
    public Object getObject() throws Exception {
        return this.obj;
    }
    @Override
    public Class<?> getObjectType() {
        return this.objType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void init() throws Exception {
        RegistryConfig registryConfig = (RegistryConfig) applicationContext.getBean("registry");
        ServiceDiscovery serviceDiscovery= registryConfig.getServiceDiscovery();
        this.url=serviceDiscovery.getNodeValue(interfaceName);
        this.objType = Class.forName(this.interfaceName);
        this.obj = ProxyServiceFactory.newServiceInstance(this.objType,url);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
