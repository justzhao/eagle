package com.zhaopeng.remote.session.tcp.manager;


import com.zhaopeng.remote.session.Connection;
import com.zhaopeng.remote.session.Session;

import com.zhaopeng.remote.session.listener.SessionListener;
import com.zhaopeng.remote.session.tcp.TcpSession;
import com.zhaopeng.remote.session.tcp.connector.TcpConnection;
import com.zhaopeng.remote.session.tcp.exchange.ExchangeTcpSessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/01
 */

@Slf4j
public class TcpSessionManager extends ExchangeTcpSessionManager {



    @Override
    public Session createSession(String sessionId, Channel channel) {
        Session session = sessions.get(sessionId);
        if (session != null) {
            log.info("session " + sessionId + " exist!");

            session.close();
            log.info("session " + sessionId + " have been closed!");
        }
        log.info("create new session " + sessionId + ", channel -> " + channel.toString());

        session = new TcpSession();
        session.setSessionId(sessionId);
        session.access();
        session.setConnection(createTcpConnection(session, channel));
        log.info("create new session " + sessionId + " successful!");

        for (SessionListener listener : sessionListeners) {
            session.addSessionListener(listener);
        }
        log.debug("add listeners to session " + sessionId + " successful! " + sessionListeners);

        return session;

    }

    protected Connection createTcpConnection(Session session, Channel channel) {
        Connection conn = new TcpConnection(channel);
        conn.setConnectionId(session.getSessionId());
        conn.setSession(session);
        return conn;
    }

}


