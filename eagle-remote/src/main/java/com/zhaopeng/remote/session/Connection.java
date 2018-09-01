package com.zhaopeng.remote.session;

public interface Connection<T> {

    void connect();

    void close();

    void send(T message);

    String getConnectionId();

    void setConnectionId(String connectionId);

    Session getSession();

    void setSession(Session session);

}
