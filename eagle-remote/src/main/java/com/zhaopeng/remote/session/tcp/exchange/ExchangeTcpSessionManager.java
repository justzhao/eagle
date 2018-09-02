package com.zhaopeng.remote.session.tcp.exchange;




import com.zhaopeng.remote.session.Session;

import io.netty.channel.Channel;


/**
 * @author zhaopeng
 * @date 2018/09/01
 */
public abstract class ExchangeTcpSessionManager extends ExchangeSessionManager {

    /**
     * create Session
     * @param sessionId
     * @param channel
     * @return
     */
    public abstract Session createSession(String sessionId, Channel channel);
}
