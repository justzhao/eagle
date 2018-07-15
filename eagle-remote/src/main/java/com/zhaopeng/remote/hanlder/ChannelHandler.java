package com.zhaopeng.remote.hanlder;

import com.zhaopeng.common.exception.RemotingException;
import io.netty.channel.Channel;

/**
 * Created by zhaopeng on 2018/7/15.
 */
public class ChannelHandler {


    public void connected(Channel channel) throws RemotingException {
    }

    public void disconnected(Channel channel) throws RemotingException {
    }


    public void sent(Channel channel, Object message) throws RemotingException {
    }

    public void received(Channel channel, Object message) throws RemotingException {
    }


    public void caught(Channel channel, Throwable ex) {

    }


}
