package com.zhaopeng.remote.session.tcp;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zhaopeng.remote.session.Connection;
import com.zhaopeng.remote.session.Session;
import com.zhaopeng.remote.session.SessionEvent;
import com.zhaopeng.remote.session.SessionManager;
import com.zhaopeng.remote.session.listener.SessionListener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/01
 */
@Slf4j
@Data
public class TcpSession implements Session {

    private String sessionId;

    private long lastAccessTime;

    private transient List<SessionListener> listeners = new CopyOnWriteArrayList<>();

    private transient Connection connection = null;

    private transient SessionManager sessionManager = null;

    protected volatile boolean isValid = false;

    protected transient volatile boolean connecting = false;

    protected transient volatile boolean closing = false;

    private Map<String, Object> attrs = new ConcurrentHashMap<>();

    private static long ACTIVE_INTERVAL = 5 * 60;

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public void access() {

        this.lastAccessTime = System.currentTimeMillis();

    }

    @Override
    public boolean isValid() {
        return System.currentTimeMillis() - lastAccessTime <= ACTIVE_INTERVAL;
    }

    @Override
    public void connect() {
        // Check to see if tellNew is in progress or has previously been called
        if (connecting || !isValid) {
            log.debug("the session " + sessionId + " is connecting or isValid = false!");
            return;
        }
        connecting = true;
        connection.connect();
        addSessionEvent();

        connecting = false;
        log.debug("the session " + sessionId + " is ready!");
    }

    private void addSessionEvent() {
        SessionEvent event = new SessionEvent(this);
        for (SessionListener listener : listeners) {
            try {
                listener.onCreated(event);
                log.info("SessionListener " + listener + " .sessionCreated() is invoked successfully!");
            } catch (Exception e) {
                log.error("addSessionEvent error.", e);
            }
        }
    }

    @Override
    public void close() {
        log.debug("the session " + sessionId + " is recycled!");

        sessionManager.removeSession(sessionId);
        listeners.clear();
        listeners = null;

        connecting = false;
        closing = false;
        sessionId = null;
        sessionManager = null;
    }

    @Override
    public void recycle() {

    }

    @Override
    public void addAttr(String name, Object obj) {
        attrs.putIfAbsent(name, obj);
    }

    @Override
    public void addSessionListener(SessionListener listener) {
        if (listener == null) {
            return;
        }
        listeners.add(listener);
    }

}
