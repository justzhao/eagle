package com.zhaopeng.eagle.invoker;


import com.zhaopeng.eagle.entity.URL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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


    //  private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));


    private InvokerServiceHandler invokerServiceHandler;

  //  private CopyOnWriteArrayList<InvokerServiceHandler> connectedHandlers = new CopyOnWriteArrayList<>();

    EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

   // protected long connectTimeoutMillis = 6000;

    private ReentrantLock lock = new ReentrantLock();

    private Condition connected = lock.newCondition();

    private AtomicInteger auto = new AtomicInteger(0);

    public InvokerBootStrap() {
        // 调用服务发现获取服务地址。
    }

    public InvokerBootStrap(String url) {
        // 调用服务发现获取服务地址。

    }

    public InvokerBootStrap(URL url) {
        // 调用服务发现获取服务地址。
        this.url = url;

    }

    public InvokerServiceHandler getInvokerServiceHandler() {
        return invokerServiceHandler;
    }

    public void setInvokerServiceHandler(InvokerServiceHandler invokerServiceHandler) {
        this.invokerServiceHandler = invokerServiceHandler;
    }

    public void connect() throws Exception {

        final List<String> urls = url.getUrls();

        if (urls == null || urls.isEmpty()) {

            logger.error("no provider");
            return;
        }

        String providerUrl = url.getProviderUrl();
        String hostPort[] = providerUrl.split(":");
        final String host = hostPort[0];
        final int port = Integer.valueOf(hostPort[1]);

        try {
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new InvokerInitializerChannel(url));
            // 发起异步连接操作
            ChannelFuture channelFuture = b.connect(host, port).sync();
            channelFuture.addListener(new NettyConnectedListener());
          //  channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放NIO线程组
            //    eventLoopGroup.shutdownGracefully();
        }

    }

/*    private void addHandler(InvokerServiceHandler handler) {
        connectedHandlers.add(handler);
        signalAvailableHandler();
    }*/

/*    public InvokerServiceHandler chooseHandler() throws InterruptedException {

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
    }*/


 /*   private boolean waitingForHandler() throws InterruptedException {
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
    }*/


    class NettyConnectedListener implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if (channelFuture.isSuccess()) {
                InvokerServiceHandler handler = channelFuture.channel().pipeline().get(InvokerServiceHandler.class);

                setInvokerServiceHandler(handler);
                //  addHandler(handler);
                logger.info("connect success");
            } else {

                logger.error("connect error");
            }
        }
    }
}
