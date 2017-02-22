package com.zhaopeng.eagle.registry.config;

/**
 * Created by zhaopeng on 2016/12/11.
 */
public class RegistryConfig {

    private String address;


    public void init() {

        // 应该要实例化zk 客户端


    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
