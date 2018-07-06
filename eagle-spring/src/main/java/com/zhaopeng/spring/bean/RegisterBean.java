package com.zhaopeng.spring.bean;

import com.zhaopeng.spring.config.AbstractConfig;

/**
 * Created by zhaopeng on 2018/7/6.
 */
public class RegisterBean extends AbstractConfig {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
