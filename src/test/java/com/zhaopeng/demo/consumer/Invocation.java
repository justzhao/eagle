package com.zhaopeng.demo.consumer;


import com.zhaopeng.demo.consumer.annotation.OrderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by zhaopeng on 2016/10/30.
 */
public class Invocation {

    public static void main(String args[]) throws IllegalAccessException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
        //StoreService storeService = (StoreService) context.getBean("storeService");
        OrderService orderService = context.getBean(OrderService.class);
        System.out.println(orderService.getOrderInfo());

     /*   StoreService storeService= ProxyServiceFactory.newServiceInstance(StoreService.class);
        System.out.println(storeService.getAllStore("我是参数")+"返回");*/

    }
}
