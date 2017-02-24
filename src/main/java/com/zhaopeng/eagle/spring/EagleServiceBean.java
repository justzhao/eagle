package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.entity.URL;
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
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class EagleServiceBean extends AbstractConfig implements ApplicationContextAware, InitializingBean, Config {

    private final static Logger logger = LoggerFactory.getLogger(EagleServiceBean.class);

    private String interfaceName;

    private String ref;

    private ApplicationContext applicationContext;

    private List<RegistryConfig> registries;


    public void init() {
        //   registryConfig = (RegistryConfig) applicationContext.getBean("registry");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {


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
        checkRegistry();
        export();

    }

    public void checkConfig() throws UnknownHostException {
        Map<String, EagleApplicationBean> configMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, EagleApplicationBean.class, false, false);
        if (configMap != null && configMap.size() > 0) {

            EagleApplicationBean applicationBean = configMap.get(0);
            port = applicationBean.getPort();
            accepts = applicationBean.getAccepts();
            threads = applicationBean.getThreads();
            host = InetAddress.getLocalHost().getHostAddress();
            protocol = applicationBean.getProtocol();
        } else {
            logger.error("没有applicationBean");
            return;
        }

    }

    public void checkRegistry() {
        Map<String, RegistryConfig> registryConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);
        if (registryConfigMap != null && registryConfigMap.size() > 0) {

            for (RegistryConfig registryConfig : registryConfigMap.values()) {
                registries.add(registryConfig);
            }
        }
    }

    public void export() {

        //注册，然后缓存实例到map
        ServiceFactory.getInstance().getHandlerMap().put(interfaceName, applicationContext.getBean(ref));
        doRegister();

    }

    public void doRegister() {
        if (registries == null || registries.size() < 1) {
            logger.error("没有注册中心");
            return;
        }
        RegistryFactory factory = new ZookeeperRegistryFactory();
        Registry registry = factory.create(registries.get(0));
        URL url = new URL(protocol,host,port,interfaceName);
        registry.register(url);

    }

    public  void  doExport(){

    }
}
