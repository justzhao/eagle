package com.zhaopeng.eagle.invoker.config;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaopeng on 2017/2/7.
 */
public class InvokerConfig {
    private ConcurrentHashMap<String,Object> sets=new ConcurrentHashMap<>();

    private static class InvokerConfigHolder{
        static InvokerConfig instance = new InvokerConfig();
    }

    public static InvokerConfig getInstance(){
        return InvokerConfig.InvokerConfigHolder.instance;
    }

    private InvokerConfig(){

    }

    public ConcurrentHashMap<String, Object> getSets() {
        return sets;
    }

    public void setSets(ConcurrentHashMap<String, Object> sets) {
        this.sets = sets;
    }
}
