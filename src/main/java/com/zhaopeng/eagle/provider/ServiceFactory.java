package com.zhaopeng.eagle.provider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class ServiceFactory {


    public ConcurrentHashMap<String,Object> handlerMap=new ConcurrentHashMap<>();

    private static class ServiceFactoryHolder{
        static ServiceFactory instance = new ServiceFactory();
    }

    public static ServiceFactory getInstance(){
        return ServiceFactoryHolder.instance;
    }


    private ServiceFactory(){

    }


    public ConcurrentHashMap<String, Object> getHandlerMap() {
        return handlerMap;
    }

    public void setHandlerMap(ConcurrentHashMap<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }
}
