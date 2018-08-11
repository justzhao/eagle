package com.zhaopeng.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhaopeng on 2018/7/3.
 */
public abstract class AbstractConfig {


    private final static Logger logger = LoggerFactory.getLogger(AbstractConfig.class);


    protected String id;


    protected String interfaceName;

    protected int port;

    protected String protocol;

    protected String host;

    protected int threads;

    protected int accepts;

    protected int timeout;

    protected int retries;

    protected String registerUrl;

    public static Logger getLogger() {
        return logger;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

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

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }
}
