package com.zhaopeng.remote.transport.impl;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.exception.RemotingException;
import com.zhaopeng.remote.transport.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
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


    public NettyServer(Url url) throws RemotingException {
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

    }

    @Override
    public void doClose() throws Throwable {

    }

    @Override
    public void send(Object message, boolean sent) {

    }

    @Override
    public void close() {

    }
}
