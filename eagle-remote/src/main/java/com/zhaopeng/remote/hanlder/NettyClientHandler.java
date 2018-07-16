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


    private final NettyChannelHandler hander;


    public NettyClientHandler(Url url, NettyChannelHandler handler) {

        this.url = url;

        this.hander = handler;
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
}
