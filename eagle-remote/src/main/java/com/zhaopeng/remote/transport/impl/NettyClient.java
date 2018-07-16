package com.zhaopeng.remote.transport.impl;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.remote.codec.Decoder;
import com.zhaopeng.remote.codec.Encoder;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.entity.Response;
import com.zhaopeng.remote.hanlder.NettyChannelHandler;
import com.zhaopeng.remote.hanlder.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * Created by zhaopeng on 2018/7/16.
 */
public class NettyClient {

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS, new DefaultThreadFactory("NettyClientWorker", true));


    private final Url url;

    private Bootstrap bootstrap;


    public NettyClient(Url url) {

        this.url = url;

        doOpen();

    }

    private void doOpen() {
        final NettyClientHandler nettyClientHandler = new NettyClientHandler(url, new NettyChannelHandler(url));
        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getTimeout())
                .channel(NioSocketChannel.class);

        if (url.getTimeOut() < 3000) {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
        } else {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, url.getTimeOut());
        }

        bootstrap.handler(new ChannelInitializer() {

            protected void initChannel(Channel ch) throws Exception {

                ch.pipeline()//.addLast("logging",new LoggingHandler(LogLevel.INFO))//for debug
                        .addLast("decoder", new Decoder(Response.class))
                        .addLast("encoder", new Encoder(Request.class))
                        .addLast("handler", nettyClientHandler);
            }
        });
    }
}
