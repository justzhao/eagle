package com.zhaopeng.registry.factory;

import com.zhaopeng.registry.Registry;
import com.zhaopeng.registry.impl.ZookeeperRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhaopeng on 2018/7/8.
 */
public class ZookeeperClientFactory {

    private static final Map<String, Registry> REGISTRIES = new ConcurrentHashMap<String, Registry>();

    private static final ReentrantLock LOCK = new ReentrantLock();

    public static Registry getRegistry(String url) {
        String key = url;

        LOCK.lock();
        try {
            Registry registry = REGISTRIES.get(key);
            if (registry != null) {
                return registry;
            }
            registry = new ZookeeperRegistry(url);
            if (registry == null) {
                throw new IllegalStateException("Can not create registry " + url);
            }
            REGISTRIES.put(key, registry);
            return registry;
        } finally {
            // Release the lock
            LOCK.unlock();
        }
    }


}
