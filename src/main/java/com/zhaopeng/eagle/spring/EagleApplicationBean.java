package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.provider.config.ServiceFactory;

/**
 * Created by zhaopeng on 2016/11/19.
 */
public class EagleApplicationBean {

    private  int port;

    private String protocol;

    public  void  init(){

       ServiceFactory instance= ServiceFactory.getInstance();
        instance.serverStart();
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
}
