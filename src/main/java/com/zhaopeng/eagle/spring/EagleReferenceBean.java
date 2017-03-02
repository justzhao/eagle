package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.invoker.ProxyServiceFactory;
import com.zhaopeng.eagle.registry.Registry;
import com.zhaopeng.eagle.registry.RegistryFactory;
import com.zhaopeng.eagle.registry.ServiceDiscovery;
import com.zhaopeng.eagle.registry.zookeeper.ZookeeperRegistryFactory;
import com.zhaopeng.eagle.spring.config.AbstractConfig;
import com.zhaopeng.eagle.spring.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * Created by zhaopeng on 2016/12/19.
 */
public class EagleReferenceBean extends AbstractConfig implements FactoryBean, ApplicationContextAware, InitializingBean, Config {



    private final static Logger logger = LoggerFactory.getLogger(EagleReferenceBean.class);


    private String interfaceName;


    private List<String> urls;

    private String url;

    private Object obj;

    private Class<?> objType;



    private ServiceDiscovery serviceDiscovery;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
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

    public ServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public Object getObject() throws Exception {
        // 返回对应的代理类吧。

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
   /*     RegistryConfig registryConfig = (RegistryConfig) applicationContext.getBean("registry");
        serviceDiscovery = new ServiceDiscovery(registryConfig.getAddress());
        this.url = serviceDiscovery.getNodeValue(interfaceName);
        this.objType = Class.forName(this.interfaceName);
        this.obj = ProxyServiceFactory.newServiceInstance(this.objType, url);

        InvokerConfig.getInstance().getSets().put("timeout", timeout);
        InvokerConfig.getInstance().getSets().put("retries", retries);*/
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // first
        checkConfig();
        refer();
    }


    public void refer() {
        doRegister();
        doRefer();
    }

    /**
     * 消费者自己注册
     */
    public void doRegister() {

        URL url = new URL(protocol, host, port, interfaceName, Constants.CONSUMER_SIDE);
        RegistryFactory factory = new ZookeeperRegistryFactory();
        Registry registry = factory.create(registries.get(0));
        registry.register(url);

    }

    /**
     * 服务订阅
     */
    public void doRefer() {

        RegistryFactory factory = new ZookeeperRegistryFactory();
        Registry registry = factory.create(registries.get(0));
        // 用于获取provider的地址
        URL url = new URL(protocol, null, port, interfaceName, Constants.PROVIDER_SIDE);
        url.setParameters(getParameters());
        urls = registry.subscribe(url);
        url.setUrls(urls);
        try {
            this.obj =createProxy(url);
        } catch (Exception e) {

            logger.error("refer service failure {}",e);

        }
    }

    private <T> T createProxy(URL url) throws Exception {
        this.objType = Class.forName(this.interfaceName);


        return  ProxyServiceFactory.newServiceInstance(url);
    }
}
