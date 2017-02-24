package com.zhaopeng.eagle.registry.zookeeper;

import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.registry.AbstractRegistry;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import com.zhaopeng.eagle.registry.config.ZookeeperConstant;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhaopeng on 2017/2/21.
 */
public class ZookeeperRegistry extends AbstractRegistry {

    private final static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private ZooKeeper zookeeper;



    public ZookeeperRegistry(){

    }

    public ZookeeperRegistry(RegistryConfig registryConfig){
        try {
            this.zookeeper=new ZooKeeper(registryConfig.getAddress(), ZookeeperConstant.TIME_OUT,new ZookeeperWatch());
        } catch (IOException e) {
            logger.error("zk 客户端 实例化失败 {}"+e);
            e.printStackTrace();
        }
    }

    class ZookeeperWatch  implements Watcher{

        @Override
        public void process(WatchedEvent event) {

        }
    }

    @Override
    public void register(URL url) {
        super.register(url);
        create(url.toString());

    }

    @Override
    public void subscribe(URL url) {
        super.subscribe(url);


    }


    /**
     *  按照路径添加节点
     * @param path
     */
    public void create(String path) {
        int i = path.lastIndexOf('/');
        if (i > 0) {
            create(path.substring(0, i));
        }
        createNode(path);
    }
    public void createNode(String path){
        try {
            zookeeper.create(path,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
