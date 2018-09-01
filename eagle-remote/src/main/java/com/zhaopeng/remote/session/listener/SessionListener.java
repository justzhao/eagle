package com.zhaopeng.remote.session.listener;

/**
 * @author zhaopeng
 * @date 2018/09/01
 */
public interface SessionListener {

    /**
     * session建立
     */
    void  onCreated();

    /**
     * session 销毁
     */
    void onDestroyed();
}
