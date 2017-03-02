package com.zhaopeng.eagle.invoker;


import com.zhaopeng.eagle.entity.URL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by zhaopeng on 2016/10/30.
 */
public class InvokerBootStrap {

    private final static Logger logger = LoggerFactory.getLogger(InvokerBootStrap.class);

    private URL url;


    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public static void main(String args[]) {
        InvokerBootStrap invokerBootStrap = new InvokerBootStrap();
        try {
            invokerBootStrap.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    protected long connectTimeoutMillis = 6000;
    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();
    private String host = "127.0.0.1";
    private int port = 8080;

    private AtomicInteger auto = new AtomicInteger(0);

    public InvokerBootStrap() {
        // 调用服务发现获取服务地址。
    }

    public InvokerBootStrap(String url) {
        // 调用服务发现获取服务地址。
        this.host = url;
    }

    public InvokerBootStrap(URL url) {
        // 调用服务发现获取服务地址。
        this.url = url;
    }

    Channel channel;
    private CopyOnWriteArrayList<InvokerServiceHandler> connectedHandlers = new CopyOnWriteArrayList<>();


    public void connect() throws Exception {

        final List<String> urls = url.getUrls();

        if (urls == null || urls.isEmpty()) {

            logger.error("no provider");
            return;
        }

        String hostPort[] = urls.get(0).split(":");
        final String host = hostPort[0];
        final int port = Integer.valueOf(hostPort[1]);
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Bootstrap b = new Bootstrap();
                    b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new InvokerInitializerChannel(url));
                    // 发起异步连接操作
                    ChannelFuture channelFuture = b.connect(host, port).sync();
                    channelFuture.addListener(new ChannelFutureListener() {
                        public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                InvokerServiceHandler handler = channelFuture.channel().pipeline().get(InvokerServiceHandler.class);
                                addHandler(handler);
                            }
                        }
                    });
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 优雅退出，释放NIO线程组
                    //    eventLoopGroup.shutdownGracefully();
                }
            }
        });
    }

    private void addHandler(InvokerServiceHandler handler) {
        connectedHandlers.add(handler);
        signalAvailableHandler();
    }

    public InvokerServiceHandler chooseHandler() throws InterruptedException {
        CopyOnWriteArrayList<InvokerServiceHandler> handlers = (CopyOnWriteArrayList<InvokerServiceHandler>) this.connectedHandlers.clone();
        int size = handlers.size();
        while (size <= 0) {
            try {
                boolean available = waitingForHandler();
                if (available) {
                    handlers = (CopyOnWriteArrayList<InvokerServiceHandler>) this.connectedHandlers.clone();
                    size = handlers.size();
                }
            } catch (InterruptedException e) {

                throw new RuntimeException("Can't connect any servers!", e);
            }
        }
        return connectedHandlers.get((auto.getAndAdd(1) + size) % size);
    }


    private boolean waitingForHandler() throws InterruptedException {
        lock.lock();
        try {
            return connected.await(this.connectTimeoutMillis, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
