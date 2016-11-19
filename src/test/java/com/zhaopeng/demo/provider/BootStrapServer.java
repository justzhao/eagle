package com.zhaopeng.demo.provider;

import com.zhaopeng.eagle.spring.EagleApplicationBean;
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

        System.in.read();
    }
}
