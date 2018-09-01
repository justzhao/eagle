package com.zhaopeng.remote.session.tcp;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zhaopeng.remote.session.Connection;
import com.zhaopeng.remote.session.Session;
import com.zhaopeng.remote.session.SessionManager;
import com.zhaopeng.remote.session.listener.SessionListener;
import io.netty.channel.Channel;
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

    private transient List<SessionListener> listeners = new CopyOnWriteArrayList<SessionListener>();

    private transient Connection connection = null;

    private transient SessionManager sessionManager = null;

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

    }

    @Override
    public void close() {

    }

    @Override
    public void recycle() {

    }

    @Override
    public void addAttr(String name, Object obj) {
        attrs.putIfAbsent(name, obj);
    }

}
