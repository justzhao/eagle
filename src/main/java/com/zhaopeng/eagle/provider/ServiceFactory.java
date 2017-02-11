package com.zhaopeng.eagle.provider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class ServiceFactory {

    /**
     * 配置map
     */
    private ConcurrentHashMap<String,Object> setsMap=new ConcurrentHashMap<>();

    public ConcurrentHashMap<String,Object> handlerMap=new ConcurrentHashMap<>();

    private static class ServiceFactoryHolder{
        static ServiceFactory instance = new ServiceFactory();
    }

    public static ServiceFactory getInstance(){
        return ServiceFactoryHolder.instance;
    }

    public void serverStart()
    {
        try {
            ProviderBootStrap.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceFactory(){

    }

    public ConcurrentHashMap<String, Object> getSetsMap() {
        return setsMap;
    }

    public void setSetsMap(ConcurrentHashMap<String, Object> setsMap) {
        this.setsMap = setsMap;
    }

    public ConcurrentHashMap<String, Object> getHandlerMap() {
        return handlerMap;
    }

    public void setHandlerMap(ConcurrentHashMap<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }
}
