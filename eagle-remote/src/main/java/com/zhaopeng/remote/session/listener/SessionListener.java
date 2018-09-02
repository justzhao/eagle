package com.zhaopeng.remote.session.listener;

import com.zhaopeng.remote.session.SessionEvent;

/**
 * @author zhaopeng
 * @date 2018/09/01
 */
public interface SessionListener {

    /**
     * session建立
     */
    void  onCreated(SessionEvent event);

    /**
     * session 销毁
     */
    void onDestroyed(SessionEvent event);
}
