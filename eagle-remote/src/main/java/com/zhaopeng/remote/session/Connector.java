package com.zhaopeng.remote.session;

public interface Connector<T> {
    void init();

    void destroy();

    void send(String sessionId, T message) throws Exception;

    boolean exist(String sessionId) throws Exception;
}
