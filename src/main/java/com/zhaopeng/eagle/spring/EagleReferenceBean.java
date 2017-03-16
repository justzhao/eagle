package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.invoker.ProxyServiceFactory;
import com.zhaopeng.eagle.registry.zookeeper.ChildListener;
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


    private List<String> urls;


    private Object obj;


    public Object getObj() {
        return obj;
    }


    public void setObj(Object obj) {
        this.obj = obj;
    }


    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public Object getObject() throws Exception {
        // 返回对应的代理类
        return this.obj;
    }

    @Override
    public Class<?> getObjectType() {
        return this.obj.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void init() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

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

        url.setType(Constants.CONSUMER_SIDE);
        registry.register(url);

    }

    /**
     * 服务订阅
     */
    public void doRefer() {

        // 用于获取provider的地址
        url.setHost(null);
        url.setType(Constants.PROVIDER_SIDE);
        url.setParameters(getParameters());
        // 订阅了服务，也需要监听。如果provider的节点发生变化，需要重新生成this.obj
        urls = registry.subscribe(url, new ChildListener() {
            @Override
            public void childChanged(String path, List<String> children) {
                EagleReferenceBean.this.notify(path, children);
            }
        });
        url.setUrls(urls);
        this.obj = createProxy(url);
    }

    private <T> T createProxy(URL url) {
        return ProxyServiceFactory.newServiceInstance(url);
    }


    /**
     * 当某个provider的节点变化的时候需要重新获取服务提供者的地址
     *
     * @param path
     * @param children
     */
    public void notify(String path, List<String> children) {
        logger.info("节点 {} 的 变化后的孩子节点是 {}", path, children);
        url.setUrls(children);
        this.obj = createProxy(url);
    }
}
