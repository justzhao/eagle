package com.zhaopeng.spring.holder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zhaopeng on 2018/7/5.
 */
public class ServiceHolder {

    /**
     * 接口名----> 实例对象
     */
    public static final ConcurrentMap<String, Object> providedServices = new ConcurrentHashMap<>();


    public static void putService(String serviceName, Object t) {
        providedServices.put(serviceName, t);
    }


    public static Object getService(String serviceName){

        return providedServices.get(serviceName);
    }

}
