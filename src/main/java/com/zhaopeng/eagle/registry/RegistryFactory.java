package com.zhaopeng.eagle.registry;

import com.zhaopeng.eagle.registry.config.RegistryConfig;

/**
 * Created by zhaopeng on 2017/2/22.
 */
public interface RegistryFactory {

    /**
     * 获取注册中心
     * @param registryConfig
     * @return
     */
    AbstractRegistry getRegister(RegistryConfig registryConfig);
}
