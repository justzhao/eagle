package com.zhaopeng.eagle.registry.zookeeper;

import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.registry.AbstractRegistry;
import com.zhaopeng.eagle.registry.config.RegistryConfig;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Created by zhaopeng on 2017/2/21.
 */
public class ZookeeperRegistry extends AbstractRegistry<IZkChildListener> {

    private final static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);


    private final ZkClient client;

    /**
     * 当前连接的状态
     */
    private Watcher.Event.KeeperState state = Watcher.Event.KeeperState.Disconnected;


    /**
     * url =》 ChildListener 和对应节点的监听器
     */
    private final ConcurrentMap<URL, ConcurrentMap<ChildListener, IZkChildListener>> zkListeners = new ConcurrentHashMap<>();


    public ZookeeperRegistry(RegistryConfig registryConfig) {

        // this.zookeeper = new ZooKeeper(registryConfig.getAddress(), ZookeeperConstant.TIME_OUT, new ZookeeperWatch());
        client = new ZkClient(registryConfig.getAddress());
        client.subscribeStateChanges(new IZkStateListener() {
            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                ZookeeperRegistry.this.state = state;
                if (state == Watcher.Event.KeeperState.Disconnected) {
                    stateChanged(StateListener.DISCONNECTED);
                } else if (state == Watcher.Event.KeeperState.SyncConnected) {
                    //重新连接的话需要重新注册节点
                    stateChanged(StateListener.CONNECTED);
                }
            }

            public void handleNewSession() throws Exception {
                stateChanged(StateListener.RECONNECTED);
            }
        });


    }

    class ZookeeperWatch implements Watcher {

        @Override
        public void process(WatchedEvent event) {

        }
    }

    @Override
    public void register(URL url) {
        super.register(url);
        create(url.toString(), true);

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

    @Override
    public List<String> subscribe(URL url, final ChildListener childListener) {
        try {
            final String path = url.getPath();

            return client.subscribeChildChanges(path, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                    childListener.childChanged(path, currentChilds);
                }
            });
        } catch (Exception e) {
            logger.error("订阅服务失败 {}", e);
        }
        return null;


    }

    public List<String> getChildren(String path) {


        List<String> urls = client.getChildren(path);
        return urls;


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

        if(client.exists(path)) return;
        // 创建临时节点
        client.createEphemeral(path);

    }

    /**
     * 创建永久节点
     *
     * @param path
     */
    public void createPersistent(String path) {
        if (!client.exists(path)) {
            client.createPersistent(path);
        }
    }


}
