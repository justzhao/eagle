package com.zhaopeng.eagle.spring.registry;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 从zk中发现服务
 * Created by zhaopeng on 2016/12/10.
 */
public class ServiceDiscovery {

    private ZooKeeper zookeeper;
    private String address;
    List<String> serviceList = new ArrayList<>();

    public ServiceDiscovery(String address) {
        this.address = address;
        initZookeeper();
        watchNode(getZookeeper());
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

    public void destroyZookeeper() {
        if (zookeeper != null) {
            try {
                zookeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String findService() {

        // 如果没有服务需要同步
        String service = null;
        service = serviceList.get(ThreadLocalRandom.current().nextInt(serviceList.size()));
        return service;
    }

    public void watchNode(final ZooKeeper zooKeeper) {
        List<String> nodeList = null;
        try {
            nodeList = zookeeper.getChildren(ZookeeperConstant.ROOT_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zooKeeper);
                    }

                }
            });
               //节点名字是接口名字，节点value是 ip地址
            for (String node : nodeList) {
                byte[] bytes = zookeeper.getData(ZookeeperConstant.ROOT_PATH + "/" + node, false, null);
                serviceList.add(new String(bytes));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public  String getNodeValue(String node){
        try {
            byte[] bytes=zookeeper.getData(ZookeeperConstant.ROOT_PATH+"/"+node,false,null);
            return new String(bytes);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return null;

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
