package com.zhaopeng.registry.client.impl;

import com.google.common.base.Strings;
import com.zhaopeng.registry.client.ZookeeperClient;
import com.zhaopeng.registry.listener.ChildListener;
import com.zhaopeng.registry.listener.StateListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by zhaopeng on 2018/7/8.
 */
public class CuratorZookeeperClient implements ZookeeperClient {

    private final ConcurrentMap<String, ConcurrentMap<ChildListener, CuratorWatcher>> childListeners
        = new ConcurrentHashMap<>();

    private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<>();

    /**
     * 客户端
     */
    private final CuratorFramework client;

    private String url;

    public CuratorZookeeperClient(String url) {
        try {
            this.url = url;
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(url)
                .retryPolicy(new RetryNTimes(1, 1000))
                .connectionTimeoutMs(5000);

            client = builder.build();
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState state) {
                    if (state == ConnectionState.LOST) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
                    } else if (state == ConnectionState.CONNECTED) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
                    } else if (state == ConnectionState.RECONNECTED) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
                    }
                }
            });
            client.start();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void create(String path, boolean ephemeral) {

        try {

            int i = path.lastIndexOf('/');
            if (i > 0) {
                String parentPath = path.substring(0, i);
                if (!checkExists(parentPath)) {
                    create(parentPath, false);
                }
            }

            if (ephemeral) {
                client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
            } else {

                client.create().forPath(path);
            }
        } catch (KeeperException.NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }

    @Override
    public void delete(String path) {
        try {
            client.delete().forPath(path);
        } catch (KeeperException.NoNodeException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }

    public boolean checkExists(String path) {
        try {
            if (client.checkExists().forPath(path) != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public List<String> addChildListener(String path, ChildListener listener) {
        ConcurrentMap<ChildListener, CuratorWatcher> listeners = childListeners.get(path);
        if (listeners == null) {
            childListeners.putIfAbsent(path, new ConcurrentHashMap<ChildListener, CuratorWatcher>());
            listeners = childListeners.get(path);
        }
        CuratorWatcher targetListener = listeners.get(listener);
        if (targetListener == null) {
            listeners.putIfAbsent(listener, new CuratorWatcherImpl(listener));
            targetListener = listeners.get(listener);
        }
        return addTargetChildListener(path, targetListener);
    }

    public List<String> addTargetChildListener(String path, CuratorWatcher listener) {
        try {
            return client.getChildren().usingWatcher(listener).forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void removeChildListener(String path, ChildListener listener) {

        ConcurrentMap<ChildListener, CuratorWatcher> listeners = childListeners.get(path);
        if (listeners != null) {
            CuratorWatcher targetListener = listeners.remove(listener);
            if (targetListener != null) {
                ((CuratorWatcherImpl)targetListener).unwatch();
            }
        }

    }

    @Override
    public void addStateListener(StateListener listener) {

        stateListeners.add(listener);
    }

    @Override
    public void removeStateListener(StateListener listener) {

        stateListeners.remove(listener);
    }

    @Override
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public String getUrl() {
        return url;
    }

    protected void stateChanged(int state) {

    }

    private class CuratorWatcherImpl implements CuratorWatcher {

        private volatile ChildListener listener;

        public CuratorWatcherImpl(ChildListener listener) {
            this.listener = listener;
        }

        public void unwatch() {
            this.listener = null;
        }

        @Override
        public void process(WatchedEvent event) throws Exception {
            if (listener != null) {
                String path = event.getPath() == null ? "" : event.getPath();
                listener.childChanged(path,
                    !Strings.isNullOrEmpty(path)
                        ? client.getChildren().usingWatcher(this).forPath(path)
                        : Collections.<String>emptyList());
            }
        }
    }

}
