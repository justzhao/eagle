package com.zhaopeng.remote.hanlder;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import io.netty.channel.*;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaopeng on 2018/7/12.
 */

@Slf4j
public class NettyServerHandler extends ChannelDuplexHandler {

    private final Url url;

    private final ExecutorService executor;

    private final ChannelHandler handler;

    private final Map<String, Channel> channels = new ConcurrentHashMap<>();

    protected static final String SERVER_THREAD_POOL_NAME = "EagleServerHandler";

    public NettyServerHandler(Url url, ChannelHandler channelHandler) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }

        this.handler = channelHandler;
        this.url = url;

        this.executor = new ThreadPoolExecutor(Constants.THREAD_POOL_CORE_SIZE, url.getThreads(),
            Constants.THREAD_POOL_ALIVE_TIME, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(Constants.THREAD_POOL_QUEUE_SIZE),new DefaultThreadFactory(SERVER_THREAD_POOL_NAME));
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

        handler.received(ctx.channel(), msg);

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        handler.sent(ctx.channel(), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception {

    }

}
