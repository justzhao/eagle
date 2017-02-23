package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.provider.ServiceFactory;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class EagleServiceBean implements ApplicationContextAware, InitializingBean {

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
        checkRegistry();


    }

    public void  checkRegistry(){
        Map<String, RegistryConfig> registryConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);
        if (registryConfigMap != null && registryConfigMap.size() > 0) {

            for(RegistryConfig registryConfig:registryConfigMap.values()){
                registries.add(registryConfig);
            }
        }
    }

    public void export(){

        //注册，然后缓存实例到map
        ServiceFactory.getInstance().getHandlerMap().put(interfaceName, applicationContext.getBean(ref));
        doRegister();
    }

    public void doRegister(){

        if(registries==null||registries.size()<1){
            logger.error("没有注册中心");
            return;
        }






    }
}
