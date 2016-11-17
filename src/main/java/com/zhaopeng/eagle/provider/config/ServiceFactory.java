package com.zhaopeng.eagle.provider.config;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class ServiceFactory {

    public  static  void serverStart()
    {
        try {
            ProviderBootStrap.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
