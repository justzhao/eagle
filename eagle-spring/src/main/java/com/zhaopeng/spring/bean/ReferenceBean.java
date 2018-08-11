package com.zhaopeng.spring.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.registry.Registry;
import com.zhaopeng.registry.impl.ZookeeperRegistry;
import com.zhaopeng.registry.listener.ChildListener;
import com.zhaopeng.remote.invoker.proxy.ProxyServiceFactory;
import com.zhaopeng.spring.config.AbstractConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhaopeng on 2018/7/4.
 */
@Slf4j
public class ReferenceBean<T> extends AbstractConfig implements ApplicationContextAware, FactoryBean, InitializingBean {

    private transient volatile T ref;

    private ApplicationContext applicationContext;

    private Registry registry;

    private Url url;

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

        url = initUrl();

        String interfaceName = url.getInterfaceName();

        registry = new ZookeeperRegistry(url.getRegisterUrl());

        url.setType(Constants.PROVIDER_SIDE);

        // 订阅了服务，也需要监听。如果provider的节点发生变化，需要重新生成this.obj
        List<String> urls = registry.subscribe(url, new ChildListener() {
            @Override
            public void childChanged(String path, List<String> children) {
                ReferenceBean.this.notify(path, children);
            }
        });
        url.setUrls(urls);
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

    private Url initUrl() {

        Url url = new Url();
        url.setInterfaceName(interfaceName);
        url.setProtocol(protocol);
        url.setRegisterAddress(registerUrl);
        url.setParameters(getParameters());
        return url;

    }

    public Map<String, String> getParameters() {

        Map<String, String> map = new HashMap<>();

        map.put(Constants.ACCEPTS_KEY, String.valueOf(accepts));
        map.put(Constants.TIMEOUT_KEY, String.valueOf(timeout));
        map.put(Constants.THREADS, String.valueOf(threads));
        map.put(Constants.RETRIES, String.valueOf(retries));
        return map;

    }

    public void notify(String path, List<String> children) {
        log.info("path {} is changed  {}", path, children);
        url.setUrls(children);
        this.ref = ProxyServiceFactory.newServiceInstance(url);

    }
}
