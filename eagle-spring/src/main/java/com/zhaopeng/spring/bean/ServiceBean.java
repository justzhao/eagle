package com.zhaopeng.spring.bean;

import com.google.common.base.Strings;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.utils.NetUtils;
import com.zhaopeng.registry.Registry;
import com.zhaopeng.registry.factory.RegisterClientFactory;
import com.zhaopeng.remote.transport.Server;
import com.zhaopeng.remote.transport.impl.TransportServer;
import com.zhaopeng.spring.config.AbstractConfig;
import com.zhaopeng.spring.handler.NettyChannelHandler;
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
    private volatile T ref;

    private volatile Server server;

    private Class<?> interfaceClass;

    private Url url;

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

    private void register() {
        ApplicationBean applicationBean = applicationContext.getBean(ApplicationBean.class);

        if (Strings.isNullOrEmpty(applicationBean.getRegisterUrl())) {
            throw new IllegalStateException("<application:service registerUrl=\"\" /> registerUrl not allow null!");
        }
        /**
         * provider调用注册
         */
        Registry registry = RegisterClientFactory.getRegistry(applicationBean.getRegisterUrl());
        url = buildUrl(applicationBean);

        url.setHost(NetUtils.getLocalHost());
        registry.registerUrl(url);

        if (server == null) {
            server = TransportServer.bind(url,new NettyChannelHandler());
        }
    }

    private Url buildUrl(ApplicationBean applicationBean) {

        Url url = new Url();
        url.setInterfaceName(interfaceName);
        url.setPort(applicationBean.getPort());
        url.setProtocol(applicationBean.getProtocol());
        url.setType("provider");
        return url;
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
