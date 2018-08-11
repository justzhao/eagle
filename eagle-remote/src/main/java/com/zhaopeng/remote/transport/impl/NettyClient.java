package com.zhaopeng.remote.transport.impl;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.exception.RemotingException;
import com.zhaopeng.common.utils.NetUtils;
import com.zhaopeng.remote.codec.Decoder;
import com.zhaopeng.remote.codec.Encoder;
import com.zhaopeng.remote.dispacher.DefaultFuture;
import com.zhaopeng.remote.dispacher.ResponseFuture;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.entity.Response;
import com.zhaopeng.remote.hanlder.NettyChannelHandler;
import com.zhaopeng.remote.hanlder.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaopeng on 2018/7/16.
 */

@Slf4j
public class NettyClient {

    private Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS,
        new DefaultThreadFactory("NettyClientWorker", true));

    private final Url url;

    private Bootstrap bootstrap;

    private volatile Channel channel;

    public NettyClient(Url url) throws Exception {

        this.url = url;

        doOpen();
        doConnected();

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
            @Override
            protected void initChannel(Channel ch) throws Exception {

                ch.pipeline()//.addLast("logging",new LoggingHandler(LogLevel.INFO))//for debug
                    .addLast("decoder", new Decoder(Response.class))
                    .addLast("encoder", new Encoder(Request.class))
                    .addLast("handler", nettyClientHandler);
            }
        });
    }

    protected void doConnected() throws Exception {
        long start = System.currentTimeMillis();
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        try {
            boolean ret = future.awaitUninterruptibly(3000, TimeUnit.MILLISECONDS);

            if (ret && future.isSuccess()) {
                Channel newChannel = future.channel();
                try {
                    // Close old channel
                    Channel oldChannel = NettyClient.this.channel;
                    if (oldChannel != null) {
                        try {

                            oldChannel.close();
                        } finally {

                        }
                    }
                } finally {
                    NettyClient.this.channel = newChannel;

                }
            } else if (future.cause() != null) {
                throw new RemotingException("client(url: " + getUrl() + ") failed to connect to server "
                    + getRemoteAddress() + ", error message is:" + future.cause().getMessage(), future.cause());
            } else {
                throw new RemotingException("client(url: " + getUrl() + ") failed to connect to server "
                    + getRemoteAddress() + " client-side timeout "
                    + getUrl().getTimeOut() + "ms (elapsed: " + (System.currentTimeMillis() - start)
                    + "ms) from netty client "
                    + NetUtils.getLocalHost());
            }
        } finally {

        }
    }

    public Url getUrl() {
        return url;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public InetSocketAddress getConnectAddress() {

        List<String> urls = getUrl().getUrls();
        if (CollectionUtils.isEmpty(urls)) {
            throw new IllegalArgumentException("interface " + getUrl().getInterfaceName() + " no provider");
        }
        String remoteUrl = urls.get(0);

        String remoteUrls[] = remoteUrl.split(":");
        if (remoteUrls == null || remoteUrls.length != 2) {
            throw new IllegalArgumentException(
                "interface " + getUrl().getInterfaceName() + " provider url illegal " + remoteUrl);
        }
        try {
            return new InetSocketAddress(remoteUrls[0], Integer.parseInt(remoteUrls[1]));
        } catch (Exception e) {

            logger.error(" getConnectAddress error {}", e);
        }
        return null;

    }

    public InetSocketAddress getRemoteAddress() {
        return getUrl().toInetSocketAddress();
    }

    public ResponseFuture sendMessage(Request req) {

        DefaultFuture future = new DefaultFuture(channel, req, url.getTimeOut());
        try {
            channel.writeAndFlush(req);
        } catch (Exception e) {
            future.cancel();
            throw e;
        }
        return future;
    }

}
