package com.zhaopeng.demo.provider;

import com.zhaopeng.eagle.spring.EagleApplicationBean;
import com.zhaopeng.eagle.spring.RegistryConfig;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhaopeng on 2016/11/15.
 */
public class BootStrapServer {

    public  static void main(String args[]) throws Exception {

        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring-server.xml");
        context.start();
        EagleApplicationBean eagleApplicationBean=(EagleApplicationBean) context.getBean("eagle");
        System.out.println(eagleApplicationBean.getPort() +" : "+eagleApplicationBean.getProtocol());
         RegistryConfig registryConfig=(RegistryConfig)context.getBean("registry");
        System.out.println(registryConfig.getAddress());

        System.in.read();
    }
}
