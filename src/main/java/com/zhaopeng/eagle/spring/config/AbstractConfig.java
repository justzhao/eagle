package com.zhaopeng.eagle.spring.config;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import com.zhaopeng.eagle.spring.EagleApplicationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaopeng on 2017/2/24.
 */
public class AbstractConfig implements Config {

    private final static Logger logger = LoggerFactory.getLogger(AbstractConfig.class);



    protected ApplicationContext applicationContext;

    protected List<RegistryConfig> registries = new ArrayList<>();

    protected int port;

    protected String protocol;

    protected String host;


    protected int threads;

    protected int accepts;

    protected int timeout;

    protected int retries;


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getAccepts() {
        return accepts;
    }

    public void setAccepts(int accepts) {
        this.accepts = accepts;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    @Override
    public void checkConfig() throws UnknownHostException {
        Map<String, EagleApplicationBean> configMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, EagleApplicationBean.class, false, false);
        if (configMap != null && configMap.size() > 0) {
            EagleApplicationBean applicationBean = configMap.get("eagle");
            port = applicationBean.getPort();
            accepts = applicationBean.getAccepts();
            threads = applicationBean.getThreads();
            host = InetAddress.getLocalHost().getHostAddress();
            protocol = applicationBean.getProtocol();
        } else {
            logger.error("没有applicationBean");
            return;
        }
        checkRegistry();
    }

    public void checkRegistry() {
        Map<String, RegistryConfig> registryConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);
        if (registryConfigMap != null && registryConfigMap.size() > 0) {

            for (RegistryConfig registryConfig : registryConfigMap.values()) {
                registries.add(registryConfig);
            }
        }
    }

    public Map<String, String> getParameters() {

        Map<String, String> map = new HashMap<>();

        map.put(Constants.ACCEPTS, String.valueOf(accepts));
        map.put(Constants.TIME_OUT, String.valueOf(timeout));
        map.put(Constants.THREADS, String.valueOf(threads));
        map.put(Constants.RETRIES, String.valueOf(retries));
        return map;

    }
}
