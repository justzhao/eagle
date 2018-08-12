package com.zhaopeng.remote.hanlder;


import com.zhaopeng.remote.entity.Response;
import io.netty.channel.Channel;

public interface ChannelHandler {

    public void connected(Channel channel);

    public void disconnected(Channel channel);

    public void sent(Channel channel, Object message);

    public void received(Channel channel, Object message);

    public void caught(Channel channel, Throwable ex);



}
