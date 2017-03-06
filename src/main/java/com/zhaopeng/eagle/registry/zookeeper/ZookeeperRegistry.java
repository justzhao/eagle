package com.zhaopeng.eagle.registry.zookeeper;

import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.registry.AbstractRegistry;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import com.zhaopeng.eagle.registry.config.ZookeeperConstant;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhaopeng on 2017/2/21.
 */
public class ZookeeperRegistry extends AbstractRegistry {

    private final static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private ZooKeeper zookeeper;


    public ZookeeperRegistry() {

    }

    public ZookeeperRegistry(RegistryConfig registryConfig) {
        try {
            this.zookeeper = new ZooKeeper(registryConfig.getAddress(), ZookeeperConstant.TIME_OUT, new ZookeeperWatch());
        } catch (IOException e) {
            logger.error("zk 客户端 实例化失败 {}" + e);
            e.printStackTrace();
        }
    }

    class ZookeeperWatch implements Watcher {

        @Override
        public void process(WatchedEvent event) {

        }
    }

    @Override
    public void register(URL url) {
        super.register(url);
        create(url.toString(),true);

    }

    @Override
    public List<String> subscribe(URL url) {

        try {
            String path = url.getPath();
            return getChildren(path);
        } catch (Exception e) {

            logger.error("get url fail {} ", e);
        }
        return null;
    }

    public List<String> getChildren(String path) {

        try {
            List<String> urls = zookeeper.getChildren(path, true);
            return urls;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 按照路径添加节点
     *
     * @param path
     * @param ephemeral 是否临时
     */
    void create(String path, boolean ephemeral) {
        int i = path.lastIndexOf('/');
        if (i > 0) {
            // 父节点都是永久的
            create(path.substring(0, i), false);
        }
        if (ephemeral) {
            createEphemeral(path);
        } else {
            createPersistent(path);
        }

    }

    /**
     * 创建临时节点
     *
     * @param path
     */
    public void createEphemeral(String path) {
        try {
            // 创建临时节点
            zookeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建永久节点
     *
     * @param path
     */
    public void createPersistent(String path) {
        try {
            // 创建临时节点
            zookeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
