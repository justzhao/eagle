package com.zhaopeng.eagle.registry;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 注册服务到zk中
 * Created by zhaopeng on 2016/12/10.
 */
public class ServiceRegistry {

    private ZooKeeper zookeeper;
    private String address;

    public ServiceRegistry(String address) {
        this.address = address;
        initZookeeper();
    }

    private void registryService(String service) {

        if (service == null) return;
        createRoot();
        addServiceNode(service);


    }


    public void initZookeeper() {

        try {
            this.zookeeper = new ZooKeeper(address, ZookeeperConstant.TIME_OUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createRoot() {

        try {
            if (zookeeper.exists(ZookeeperConstant.ROOT_PATH, false) == null) {
                zookeeper.create(ZookeeperConstant.ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void addServiceNode(String service) {
        byte[] bytes = service.getBytes();
        try {
            zookeeper.create(ZookeeperConstant.NODE_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public ZooKeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
