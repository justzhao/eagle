package com.zhaopeng.demo.provider.annotation;

import com.zhaopeng.demo.api.StoreService;
import com.zhaopeng.eagle.annotation.Service;

/**
 * Created by zhaopeng on 2017/3/14.
 */

@Service
public class StoreServiceImpl implements StoreService {

    @Override
    public String getAllStore(String input) {
        return "the store id is "+input;
    }
}