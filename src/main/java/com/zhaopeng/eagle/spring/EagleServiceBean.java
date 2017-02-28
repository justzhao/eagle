package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.provider.ProviderBootStrap;
import com.zhaopeng.eagle.provider.ServiceFactory;
import com.zhaopeng.eagle.registry.Registry;
import com.zhaopeng.eagle.registry.RegistryFactory;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import com.zhaopeng.eagle.registry.zookeeper.ZookeeperRegistryFactory;
import com.zhaopeng.eagle.spring.config.AbstractConfig;
import com.zhaopeng.eagle.spring.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class EagleServiceBean extends AbstractConfig implements ApplicationContextAware, InitializingBean, Config {

    private final static Logger logger = LoggerFactory.getLogger(EagleServiceBean.class);

    private String interfaceName;

    private String ref;




    private volatile boolean isExported;


    public void init() {

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

    public List<RegistryConfig> getRegistries() {
        return registries;
    }

    public void setRegistries(List<RegistryConfig> registries) {
        this.registries = registries;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RegistryConfig registryConfig = (RegistryConfig) applicationContext.getBean("registry");

        // serviceRegistry = new ServiceRegistry(registryConfig.getAddress());
        //String host = InetAddress.getLocalHost().getHostAddress();

        // serviceRegistry.addNode(interfaceName, host);
        checkConfig();

        export();

    }

    public void export() {

        //注册，然后缓存实例到map
        ServiceFactory.getInstance().getHandlerMap().put(interfaceName, applicationContext.getBean(ref));
        doRegister();
        doExport();

    }

    /**
     * 注册
     */
    public void doRegister() {
        if (registries == null || registries.size() < 1) {
            logger.error("没有注册中心");
            return;
        }
        RegistryFactory factory = new ZookeeperRegistryFactory();
        Registry registry = factory.create(registries.get(0));
        URL url = new URL(protocol, host, port, interfaceName, Constants.PROVIDER_SIDE);
        registry.register(url);

    }

    /**
     * 启动netty，暴露服务。
     */
    public synchronized void doExport() {

        if (isExported) return;

        isExported = true;
        logger.info("start netty");

        try {
            ProviderBootStrap.init(port);
        } catch (Exception e) {

            logger.error("fail start netty {}", e);
        }
        logger.info("netty init success");


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
