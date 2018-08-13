package com.zhaopeng.remote.hanlder;



import java.util.concurrent.ExecutionException;

import io.netty.channel.Channel;

public interface ChannelHandler {

    public void connected(Channel channel);

    public void disconnected(Channel channel);

    public void sent(Channel channel, Object message);

    public void received(Channel channel, Object message) throws ExecutionException;

    public void caught(Channel channel, Throwable ex);



}
