package com.zhaopeng.remote.session.tcp.exchange;


import java.util.List;

import com.zhaopeng.remote.session.Session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhaopeng
 * @date 2018/09/01
 */
public abstract class ExchangeTcpSessionManager extends ExchangeSessionManager {

    /**
     * create Session
     * @param sessionId
     * @param ctx
     * @return
     */
    public abstract Session createSession(String sessionId, ChannelHandlerContext ctx);
}
