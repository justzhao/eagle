package com.zhaopeng.eagle.invoker;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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
public class InvokerConfig {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    protected long connectTimeoutMillis = 6000;
    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();
    static String host = "127.0.0.1";
    static int port = 8080;

    private AtomicInteger auto = new AtomicInteger(0);

    Channel channel;
    private CopyOnWriteArrayList<InvokerServiceHandler> connectedHandlers = new CopyOnWriteArrayList<>();


    public  void connect() throws Exception {


        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {

                try {
                    Bootstrap b = new Bootstrap();

                    b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new RpcInvokerInitializer());
                    // 发起异步连接操作
                    ChannelFuture channelFuture = b.connect(host, port).sync();
                   channelFuture.addListener(new ChannelFutureListener() {

                        public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                System.out.println("Successfully connect to remote server. remote peer = " );
                                InvokerServiceHandler handler = channelFuture.channel().pipeline().get(InvokerServiceHandler.class);
                                addHandler(handler);
                            }
                        }
                    });
                   channelFuture.channel().closeFuture().sync();

                    System.out.println("over");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 优雅退出，释放NIO线程组
                    eventLoopGroup.shutdownGracefully();
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
        while (  size <= 0) {
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
