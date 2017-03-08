package com.zhaopeng.eagle.registry;

import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.registry.zookeeper.ChildListener;

import java.util.List;

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
     List<String> subscribe(URL url);

    /**
     * 订阅服务并且添加监听器
     * @param url
     * @param childListener
     * @return
     */
    List<String> subscribe(URL url, ChildListener childListener);
}
