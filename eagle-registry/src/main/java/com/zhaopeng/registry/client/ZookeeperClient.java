package com.zhaopeng.registry.client;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.registry.listener.ChildListener;
import com.zhaopeng.registry.listener.StateListener;

import java.util.List;

/**
 * Created by zhaopeng on 2018/7/8.
 */
public interface ZookeeperClient {

    void create(String path, boolean ephemeral);

    void delete(String path);

    List<String> getChildren(String path);

    List<String> addChildListener(String path, ChildListener listener);

    void removeChildListener(String path, ChildListener listener);

    void addStateListener(StateListener listener);

    void removeStateListener(StateListener listener);

    boolean isConnected();

    void close();

    Url getUrl();
}
