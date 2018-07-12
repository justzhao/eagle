package com.zhaopeng.remote.transport.impl;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.exception.RemotingException;
import com.zhaopeng.remote.codec.Decoder;
import com.zhaopeng.remote.codec.Encoder;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.entity.Response;
import com.zhaopeng.remote.hanlder.NettyServerHandler;
import com.zhaopeng.remote.transport.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaopeng on 2018/7/10.
 */
public class NettyServer implements Server {


    protected static final String SERVER_THREAD_POOL_NAME = "DubboServerHandler";
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    ExecutorService executor;
    private InetSocketAddress localAddress;
    private InetSocketAddress bindAddress;
    private int accepts;

    private int idleTimeout;
    /**
     * <ip:port, channel>
     */
    private Map<String, Channel> channels;

    private ServerBootstrap bootstrap;

    private io.netty.channel.Channel channel;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private final Url url;

    public NettyServer(Url url) throws RemotingException {

        this.url = url;
        String bindIp = url.getHost();
        int bindPort = url.getPort();


        bindAddress = new InetSocketAddress(bindIp, bindPort);
        this.accepts = url.getParameter(Constants.ACCEPTS_KEY, Constants.DEFAULT_ACCEPTS);
        this.idleTimeout = url.getParameter(Constants.IDLE_TIMEOUT_KEY, Constants.DEFAULT_IDLE_TIMEOUT);
        try {
            doOpen();
            if (logger.isInfoEnabled()) {
                logger.info("Start " + getClass().getSimpleName() + " bind " + bindIp + ", export " + bindPort);
            }
        } catch (Throwable t) {
            throw new RemotingException("Failed to bind " + getClass().getSimpleName()
                    + " on " + bindIp + ", cause: " + t.getMessage(), t);
        }

        executor = new ThreadPoolExecutor(Constants.THREAD_POOL_CORE_SIZE, url.getThreads(), Constants.THREAD_POOL_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(Constants.THREAD_POOL_QUEUE_SIZE));

    }


    @Override
    public void doOpen() throws Throwable {


        bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        workerGroup = new NioEventLoopGroup(getUrl().getPositiveParameter(Constants.IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS),
                new DefaultThreadFactory("NettyServerWorker", true));

        final NettyServerHandler nettyServerHandler = new NettyServerHandler(getUrl());
        channels = nettyServerHandler.getChannels();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                        ch.pipeline().addLast("decoder",new Decoder(Request.class))
                                .addLast("encoder", new Encoder(Response.class))
                                .addLast("handler", nettyServerHandler);
                    }
                });
        // bind
        ChannelFuture channelFuture = bootstrap.bind(getBindAddress());
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
    }

    @Override
    public void send(Object message, boolean sent) {

        /**
         *发送消息出去
         */

    }




    @Override
    public void doClose() throws Throwable {
        try {
            if (channel != null) {
                // unbind.
                channel.close();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }

        try {
            if (bootstrap != null) {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            if (channels != null) {
                channels.clear();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }





    public boolean isBound() {
        return channel.isActive();
    }


    public Url getUrl() {
        return url;
    }

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }
}
