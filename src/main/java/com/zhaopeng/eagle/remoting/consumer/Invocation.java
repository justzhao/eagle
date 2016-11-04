package com.zhaopeng.eagle.remoting.consumer;

import com.zhaopeng.eagle.invoker.ProxyServiceFactory;
import com.zhaopeng.eagle.remoting.api.StoreService;



/**
 * Created by zhaopeng on 2016/10/30.
 */
public class Invocation {

    public  static  void  main(String args[]) throws IllegalAccessException {

        StoreService storeService= ProxyServiceFactory.newServiceInstance(StoreService.class);

        System.out.println(storeService.getAllStore("我是参数")+"返回");

    }
}
