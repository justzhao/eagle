package com.zhaopeng.spring.bean;

/**
 * Created by zhaopeng on 2018/7/4.
 */
public class ApplicationBean {

    /**
     * 启动的端口
     */
    private int port;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 服务端的业务线程池个数
     */
    private int threads;

    /**
     * 服务端可接受的连接数
     */
    private int accepts;

    /**
     * 注册中心的地址
     */
    private String registerUrl;

    /**
     * 应用名称
     */
    private String applicationName;




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


    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
