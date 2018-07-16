package com.zhaopeng.registry.impl;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.registry.Registry;
import com.zhaopeng.registry.client.ZookeeperClient;
import com.zhaopeng.registry.client.impl.CuratorZookeeperClient;

/**
 * Created by zhaopeng on 2018/7/6.
 */
public class ZookeeperRegistry implements Registry {

    private final ZookeeperClient zookeeperClient;

    public ZookeeperRegistry(String url) {
        zookeeperClient = new CuratorZookeeperClient(url);
    }

    @Override
    public void registerUrl(Url url) {

        zookeeperClient.create(url.buildUrlString(), true);
    }
}
