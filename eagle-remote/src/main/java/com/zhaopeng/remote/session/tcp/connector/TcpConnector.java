package com.zhaopeng.remote.session.tcp.connector;

import com.zhaopeng.remote.MessageWrapper;
import com.zhaopeng.remote.session.Session;
import com.zhaopeng.remote.session.listener.TcpHeartbeatListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */

@Slf4j
public class TcpConnector extends ExchangeTcpConnector{

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
    public void connect(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        try {
            Session session = tcpSessionManager.createSession(wrapper.getSessionId(), ctx);
            session.addSessionListener(tcpHeartbeatListener);
            session.connect();

            tcpSessionManager.addSession(session);
            /** send **/
            session.getConnection().send(wrapper.getBody());
        } catch (Exception e) {
            log.error("TcpConnector connect occur Exception.", e);
        }
    }

    @Override
    public void close(MessageWrapper wrapper) {
        Session session = tcpSessionManager.getSession(wrapper.getSessionId());
        session.getConnection().send(wrapper.getBody());
        session.close();
    }

    @Override
    public void heartbeatClient(MessageWrapper wrapper) {
        try {
            tcpSessionManager.updateSession(wrapper.getSessionId());
            Session session = tcpSessionManager.getSession(wrapper.getSessionId());
            session.getConnection().send(wrapper.getBody());
        } catch (Exception e) {
            log.error("TcpConnector heartbeatClient occur Exception.", e);
        }
    }

    @Override
    public void responseSendMessage(MessageWrapper wrapper) {
        try {
            Session session = tcpSessionManager.getSession(wrapper.getSessionId());
            session.getConnection().send(wrapper.getBody());
        } catch (Exception e) {
            log.error("TcpConnector responseSendMessage occur Exception.", e);
        }
    }

    @Override
    public void responseNoKeepAliveMessage(ChannelHandlerContext ctx, MessageWrapper wrapper) {
        try {
            NoKeepAliveTcpConnection connection = new NoKeepAliveTcpConnection(ctx);
            connection.send(wrapper.getBody());
        } catch (Exception e) {
            log.error("TcpConnector responseNoKeepAliveMessage occur Exception.", e);
        }
    }


}
