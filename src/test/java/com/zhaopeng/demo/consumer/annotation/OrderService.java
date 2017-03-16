package com.zhaopeng.demo.consumer.annotation;

import com.zhaopeng.demo.api.StoreService;
import com.zhaopeng.eagle.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * Created by zhaopeng on 2017/3/16.
 */
@Component
public class OrderService {
    @Reference
    private StoreService storeService;

    public String getOrderInfo() {
        return storeService.getAllStore("注解调用");
    }
}
