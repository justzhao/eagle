package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.spring.registry.ServiceDiscovery;
import com.zhaopeng.eagle.spring.registry.ServiceRegistry;

/**
 * Created by zhaopeng on 2016/12/11.
 */
public class RegistryConfig {

    private  String address;

    private ServiceRegistry serviceRegistry;

    private ServiceDiscovery serviceDiscovery;

    public void init(){
        this.serviceRegistry=new ServiceRegistry(address);
        this.serviceDiscovery=new ServiceDiscovery(address);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public ServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }
}
