package com.zhaopeng.remote.session.tcp.connector;

import com.zhaopeng.remote.MessageWrapper;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.session.Session;
import com.zhaopeng.remote.session.tcp.manager.TcpSessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */
public abstract class ExchangeTcpConnector<T> extends ExchangeConnector<T> {

    protected TcpSessionManager tcpSessionManager = null;

    public abstract void connect(Channel channel, Request request);

    public abstract void close(Request request);

    /**
     * 会话心跳
     *
     * @param wrapper
     */
    public abstract void heartbeatClient(Request request);

    /**
     * 接收客户端消息通知响应
     *
     * @param wrapper
     */
    public abstract void responseSendMessage(Request request);

    public abstract void responseNoKeepAliveMessage(Channel channel, Request request);
    @Override
    public void send(String sessionId, T message) throws Exception {
        super.send(tcpSessionManager, sessionId, message);
    }

    public boolean exist(String sessionId) throws Exception {
        Session session = tcpSessionManager.getSession(sessionId);
        return session != null ? true : false;
    }

    public void setTcpSessionManager(TcpSessionManager tcpSessionManager) {
        this.tcpSessionManager = tcpSessionManager;
    }




}
