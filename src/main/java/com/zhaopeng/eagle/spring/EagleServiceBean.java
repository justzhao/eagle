package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.provider.config.ServiceFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhaopeng on 2016/11/17.
 */
public class EagleServiceBean implements ApplicationContextAware {

    private String interfaceName;

    private String ref;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("the ref is "+ref+" "+applicationContext.getBean(ref));
        ServiceFactory.getInstance().getHandlerMap().put(interfaceName,applicationContext.getBean(ref));

        // 在这里设置好对应的service方法到 map中
        // MessageRecvExecutor.getInstance().getHandlerMap().put(interfaceName, applicationContext.getBean(ref));

    }


    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
