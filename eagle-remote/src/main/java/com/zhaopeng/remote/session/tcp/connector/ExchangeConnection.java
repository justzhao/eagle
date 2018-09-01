package com.zhaopeng.remote.session.tcp.connector;

import com.zhaopeng.remote.session.Connection;
import com.zhaopeng.remote.session.Session;

/**
 * @author zhaopeng
 * @date 2018/09/01
 */
public abstract class ExchangeConnection<T> implements Connection<T> {

    protected Session session = null;

    protected String connectionId = null;

    protected volatile boolean close = false;

    protected int connectTimeout = 60 * 60 * 1000;

    public void fireError(RuntimeException e) {
        throw e;
    }

    public boolean isClosed() {
        return close;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
