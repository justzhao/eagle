package com.zhaopeng.demo.provider;

import com.zhaopeng.demo.api.StoreService;

/**
 * Created by zhaopeng on 2016/11/15.
 */
public class StoreServiceImpl implements StoreService {
    @Override
    public String getAllStore(String input) {
        System.out.println("被调用了 ！");
        String result="provider is invoking";
        return result;
    }
}
