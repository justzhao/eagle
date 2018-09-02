package com.zhaopeng.remote.session.tcp.connector;


import com.zhaopeng.common.exception.DispatchException;
import com.zhaopeng.common.exception.PushException;
import com.zhaopeng.remote.session.Connector;
import com.zhaopeng.remote.session.Session;
import com.zhaopeng.remote.session.SessionManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */
@Slf4j
public abstract class ExchangeConnector<T> implements Connector<T> {


    public void send(SessionManager sessionManager, String sessionId, T message) throws Exception {
        Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            throw new Exception(String.format("session {} no exist.", sessionId));
        }
        try {
            session.getConnection().send(message);
            session.access();
        } catch (PushException e) {
            log.error("ExchangeConnector send occur PushException.", e);
            session.close();
            throw new DispatchException(e);
        } catch (Exception e) {
            log.error("ExchangeConnector send occur Exception.", e);
            session.close();
            throw new DispatchException(e);
        }
    }

}
