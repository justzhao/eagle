package com.zhaopeng.remote.hanlder;

import com.zhaopeng.common.bean.Url;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Created by zhaopeng on 2018/7/16.
 */
public class NettyClientHandler extends ChannelDuplexHandler {

    private Url url;


    private final ChannelHandler handler;


    public NettyClientHandler(Url url, ChannelHandler handler) {

        this.url = url;

        this.handler = handler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //接收到消息
        handler.received(ctx.channel(), msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        handler.sent(ctx.channel(),msg);
    }
}
