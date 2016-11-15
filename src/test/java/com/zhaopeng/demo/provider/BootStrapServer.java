package com.zhaopeng.demo.provider;

import com.zhaopeng.eagle.provider.ProviderConfig;

/**
 * Created by zhaopeng on 2016/11/15.
 */
public class BootStrapServer {

    public  static void main(String args[]) throws Exception {
        new ProviderConfig().bind(8080);

    }
}
