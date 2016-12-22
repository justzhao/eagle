package com.zhaopeng.demo.consumer;

import com.zhaopeng.demo.api.StoreService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by zhaopeng on 2016/10/30.
 */
public class Invocation {

    public  static  void  main(String args[]) throws IllegalAccessException {

        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-client.xml");
        StoreService storeService= (StoreService) context.getBean("storeService");
        System.out.println(storeService.getAllStore("我是参数")+"返回");

     /*   StoreService storeService= ProxyServiceFactory.newServiceInstance(StoreService.class);
        System.out.println(storeService.getAllStore("我是参数")+"返回");*/

    }
}
