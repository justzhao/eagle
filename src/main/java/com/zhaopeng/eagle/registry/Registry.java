package com.zhaopeng.eagle.registry;

import com.zhaopeng.eagle.entity.URL;

/**
 * Created by zhaopeng on 2017/2/23.
 */
public interface Registry {

    /**
     * 注册服务
     * @param url
     */
     void register(URL url);
    /**
     * 订阅服务
     * @param url
     */
     void subscribe(URL url);
}
