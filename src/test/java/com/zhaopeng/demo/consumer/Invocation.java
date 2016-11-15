package com.zhaopeng.demo.consumer;

import com.zhaopeng.demo.api.StoreService;
import com.zhaopeng.eagle.invoker.ProxyServiceFactory;



/**
 * Created by zhaopeng on 2016/10/30.
 */
public class Invocation {

    public  static  void  main(String args[]) throws IllegalAccessException {

        StoreService storeService= ProxyServiceFactory.newServiceInstance(StoreService.class);

        System.out.println(storeService.getAllStore("我是参数")+"返回");

    }
}
