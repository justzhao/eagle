package com.zhaopeng.eagle.registry.zookeeper;

import com.zhaopeng.eagle.registry.Registry;
import com.zhaopeng.eagle.registry.RegistryFactory;
import com.zhaopeng.eagle.registry.config.RegistryConfig;

/**
 * Created by zhaopeng on 2017/2/24.
 */
public class ZookeeperRegistryFactory  extends RegistryFactory {
    @Override
    public Registry create(RegistryConfig registryConfig) {
        return new ZookeeperRegistry(registryConfig);
    }
}
