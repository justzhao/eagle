package com.zhaopeng.remote.hanlder;



import java.util.concurrent.ExecutionException;

import com.zhaopeng.remote.entity.Request;
import io.netty.channel.Channel;

public interface ChannelHandler {

    public void connected(Channel channel);

    public void disconnected(Channel channel);

    public void sent(Channel channel, Request request);

    public void received(Channel channel,Request request) throws ExecutionException;

    public void caught(Channel channel, Throwable ex);



}
