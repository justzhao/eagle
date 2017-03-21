package com.zhaopeng.eagle.registry;

import com.zhaopeng.eagle.entity.URL;
import com.zhaopeng.eagle.registry.zookeeper.StateListener;
import com.zhaopeng.eagle.util.ConcurrentHashSet;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 服务的注册和发现
 * Created by zhaopeng on 2017/2/21.
 */
public abstract class AbstractRegistry<TargetChildListener> implements Registry {

    // URL地址分隔符，用于文件缓存中，服务提供者URL分隔
    private static final char URL_SEPARATOR = ' ';

    // URL地址分隔正则表达式，用于解析文件缓存中服务提供者URL列表
    private static final String URL_SPLIT = "\\s+";

    private URL registryUrl;

    // 已经注册的服务连接
    protected final Set<String> registered = new ConcurrentHashSet<>();

    /**
     * zk连接状态监听
     */
    private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<>();

    // 本地磁盘缓存文件
    private File file;


    public URL getRegistryUrl() {
        return registryUrl;
    }

    public void setRegistryUrl(URL registryUrl) {
        this.registryUrl = registryUrl;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 注册服务
     * @param url
     */
    public void register(URL url){
        registered.add(url.toString());
    }

    /**
     * 订阅服务
     * @param url
     */
    public List<String> subscribe(URL url){

        return null;
    }


    /**
     * 连接状态变化
     * @param state
     */
    protected void stateChanged(int state) {
        for (StateListener sessionListener : getSessionListeners()) {
            sessionListener.stateChanged(state);
        }
    }


    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }

    public void removeStateListener(StateListener listener) {
        stateListeners.remove(listener);
    }

    public Set<StateListener> getSessionListeners() {
        return stateListeners;
    }
}
