package com.zhaopeng.remote.session.listener;

import com.zhaopeng.remote.session.SessionEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */
@Slf4j
public class LogSessionListener implements SessionListener {
    @Override
    public void onCreated(SessionEvent event) {

        log.info("session {} created", event.getSession().getSessionId());
    }

    @Override
    public void onDestroyed(SessionEvent event) {
        log.info("session {} destroyeds", event.getSession().getSessionId());
    }
}
