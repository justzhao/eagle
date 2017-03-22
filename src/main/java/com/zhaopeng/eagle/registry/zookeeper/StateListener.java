package com.zhaopeng.eagle.registry.zookeeper;

/**
 * Created by zhaopeng on 2017/3/7.
 */
public interface StateListener {

    int DISCONNECTED = 0;

    int CONNECTED = 1;

    int RECONNECTED = 2;

    /**
     *  连接状态变化
     * @param connected
     */
    void stateChanged(int connected);
}
