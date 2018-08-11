package com.zhaopeng.registry.impl;

import java.util.List;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.registry.Registry;
import com.zhaopeng.registry.client.ZookeeperClient;
import com.zhaopeng.registry.client.impl.CuratorZookeeperClient;
import com.zhaopeng.registry.listener.ChildListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhaopeng on 2018/7/6.
 */


@Slf4j
public class ZookeeperRegistry implements Registry {

    private final ZookeeperClient client;

    public ZookeeperRegistry(String url) {
        client = new CuratorZookeeperClient(url);
    }

    @Override
    public void registerUrl(Url url) {

        client.create(url.buildUrlString(), true);
    }

    @Override
    public List<String> subscribe(Url url, ChildListener childListener) {
        try {
            final String path = url.getPath();

            return client.addChildListener(path,childListener );
        } catch (Exception e) {
           log.error("订阅服务失败 {}", e);
        }
        return null;
    }

}
