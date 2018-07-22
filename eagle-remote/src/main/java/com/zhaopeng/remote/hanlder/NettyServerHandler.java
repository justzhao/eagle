package com.zhaopeng.remote.hanlder;

import com.zhaopeng.common.bean.Url;
import io.netty.channel.*;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaopeng on 2018/7/12.
 */
public class NettyServerHandler extends ChannelDuplexHandler {

    private Url url;


    private final NettyChannelHandler hander;


    // <ip:port, channel>
    private final Map<String, Channel> channels = new ConcurrentHashMap<>();


    public NettyServerHandler(Url url, NettyChannelHandler channelHandler) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }

        this.hander = channelHandler;
        this.url = url;
    }

    public Map<String, Channel> getChannels() {
        return channels;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }


    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise future)
            throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //接收到消息

        hander.received(ctx.channel(), msg);


    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        hander.write(ctx,msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {

    }


}
