package com.zhaopeng.registry.listener;

/**
 * Created by zhaopeng on 2018/7/8.
 */
public interface StateListener {

    int DISCONNECTED = 0;

    int CONNECTED = 1;

    int RECONNECTED = 2;

    void stateChanged(int connected);
}
