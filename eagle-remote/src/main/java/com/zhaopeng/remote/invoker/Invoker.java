package com.zhaopeng.remote.invoker;

import com.zhaopeng.common.bean.Url;

/**
 * Created by zhaopeng on 2018/7/22.
 */
public class Invoker {

    private Url url;

    private String interfaceName;

    public Invoker() {
    }


    public Invoker(Url url) {

        this.url = url;

    }

    public void init() {

        String registerUrl = url.getRegisterUrl();
        String interfaceName = url.getInterfaceName();

    }

    public Object invoker(String interfaceName, String methodName, Object[] args) {


        return null;
    }
}
