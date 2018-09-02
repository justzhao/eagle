package com.zhaopeng.remote.session.tcp.connector;


import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.session.Session;
import com.zhaopeng.remote.session.listener.TcpHeartbeatListener;
import io.netty.channel.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */

@Slf4j
public class TcpConnector extends ExchangeTcpConnector {

    private TcpHeartbeatListener tcpHeartbeatListener = null;

    @Override
    public void init() {
        tcpHeartbeatListener = new TcpHeartbeatListener(tcpSessionManager);

        Thread heartbeatThread = new Thread(tcpHeartbeatListener, "tcpHeartbeatListener");
        heartbeatThread.setDaemon(true);
        heartbeatThread.start();
    }

    @Override
    public void destroy() {
        tcpHeartbeatListener.stop();

        for (Session session : tcpSessionManager.listAllSessions()) {
            session.close();
        }
        tcpSessionManager = null;
    }

    @Override
    public void connect(Channel channel, Request request) {
        try {
            Session session = tcpSessionManager.createSession(request.getSessionId(), channel);
            session.addSessionListener(tcpHeartbeatListener);
            session.connect();

            tcpSessionManager.addSession(session);
            /** send **/
            session.getConnection().send(request.getBody());
        } catch (Exception e) {
            log.error("TcpConnector connect occur Exception.", e);
        }
    }

    @Override
    public void close(Request request) {
        Session session = tcpSessionManager.getSession(request.getSessionId());
        session.getConnection().send(request.getBody());
        session.close();
    }

    @Override
    public void heartbeatClient(Request request) {
        try {
            tcpSessionManager.updateSession(request.getSessionId());
            Session session = tcpSessionManager.getSession(request.getSessionId());
            session.getConnection().send(request.getBody());
        } catch (Exception e) {
            log.error("TcpConnector heartbeatClient occur Exception.", e);
        }
    }

    @Override
    public void responseSendMessage(Request request) {
        try {
            Session session = tcpSessionManager.getSession(request.getSessionId());
            session.getConnection().send(request.getBody());
        } catch (Exception e) {
            log.error("TcpConnector responseSendMessage occur Exception.", e);
        }
    }

    @Override
    public void responseNoKeepAliveMessage(Channel channel, Request request) {
        try {
            NoKeepAliveTcpConnection connection = new NoKeepAliveTcpConnection(channel);
            connection.send(request.getBody());
        } catch (Exception e) {
            log.error("TcpConnector responseNoKeepAliveMessage occur Exception.", e);
        }
    }

}
