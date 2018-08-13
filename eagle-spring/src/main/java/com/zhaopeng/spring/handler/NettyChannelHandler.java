package com.zhaopeng.spring.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.remote.dispacher.DefaultFuture;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.entity.Response;
import com.zhaopeng.remote.hanlder.ChannelHandler;

import com.zhaopeng.spring.holder.ServiceHolder;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyChannelHandler implements ChannelHandler {

    private final ExecutorService executor;

    protected static final String SERVER_THREAD_POOL_NAME = "EagleServerHandler";

    private final Url url;
    public NettyChannelHandler(Url url) {

        this.url=url;
        this.executor = new ThreadPoolExecutor(Constants.THREAD_POOL_CORE_SIZE, url.getParameter(Constants.THREADS,Constants.DEFAULT_THREADS),
            Constants.THREAD_POOL_ALIVE_TIME, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(Constants.THREAD_POOL_QUEUE_SIZE),
            new DefaultThreadFactory(SERVER_THREAD_POOL_NAME));
    }

    public Url getUrl() {
        return url;
    }

    @Override
    public void connected(Channel channel) {

    }

    @Override
    public void disconnected(Channel channel) {

    }

    public void write(Channel channel, Object message) {

        channel.write(message);
    }

    @Override
    public void sent(Channel channel, Object message) {

        executor.execute(new ChannelEventRunnable(channel, ChannelState.SENT, this, message));

        if (message instanceof Request) {
            new DefaultFuture(channel, (Request)message, 0);
            channel.writeAndFlush(message);

        } else {
            channel.writeAndFlush(message);
            log.error("not request {}", message);
        }

    }

    public void reply(Channel channel, Object message) {

        if (message instanceof Request) {
            Request request = (Request)message;
            if (request.isHeartEvent()) {
                Response response = new Response(request.getRequestId());
                channel.writeAndFlush(response);
            } else {
                if (request.isTwoWay()) {
                    Response response = new Response(request.getRequestId());
                    try {
                        response.setResult(handle(request));
                    } catch (Throwable throwable) {

                    } finally {
                        channel.writeAndFlush(response);
                    }
                }
            }
        } else if (message instanceof Response) {
            handleResponse(channel, (Response)message);
        }
    }

    @Override
    public void received(Channel channel, Object message) throws ExecutionException {



        try {
            executor.execute(new ChannelEventRunnable(channel, ChannelState.RECEIVED, this, message));
        } catch (Throwable t) {
            //TODO A temporary solution to the problem that the exception information can not be sent to the opposite end after the thread pool is full. Need a refactoring
            //fix The thread pool is full, refuses to call, does not return, and causes the consumer to wait for time out
            if(message instanceof Request &&
                t instanceof RejectedExecutionException){
                Request request = (Request)message;
                if(request.isTwoWay()){
                    String msg = "Server side("+url.getIp()+","+url.getPort()+") threadpool is exhausted ,detail msg:"+t.getMessage();
                    Response response = new Response(request.getRequestId());
                    response.setStatus(Response.SERVER_THREADPOOL_EXHAUSTED_ERROR);
                    channel.write(response);
                    return;
                }
            }
            throw new ExecutionException(message+ " error when process received event .",t);
        }

    }

    @Override
    public void caught(Channel channel, Throwable ex) {

    }

    public void handleResponse(Channel channel, Response response) {

    }

    public Object handle(Request request) {

        ServiceHolder.getService(request.getClassName());

        return null;

    }

    /**
     * ChannelState
     */
    public enum ChannelState {

        /**
         * CONNECTED
         */
        CONNECTED,

        /**
         * DISCONNECTED
         */
        DISCONNECTED,

        /**
         * SENT
         */
        SENT,

        /**
         * RECEIVED
         */
        RECEIVED,

        /**
         * CAUGHT
         */
        CAUGHT
    }

    class ChannelEventRunnable implements Runnable {

        private final Channel channel;
        private final ChannelState state;
        private final Throwable exception;
        private final Object message;

        private final ChannelHandler handler;

        public ChannelEventRunnable(Channel channel, ChannelState state, ChannelHandler handler) {
            this(channel, state, handler, null);
        }

        public ChannelEventRunnable(Channel channel, ChannelState state, ChannelHandler handler, Object message) {
            this(channel, state, handler, message, null);
        }

        public ChannelEventRunnable(Channel channel, ChannelState state, ChannelHandler handler, Throwable t) {
            this(channel, state, handler, null, t);
        }

        public ChannelEventRunnable(Channel channel, ChannelState state, ChannelHandler handler, Object message,
                                    Throwable exception) {
            this.channel = channel;
            this.state = state;
            this.handler = handler;
            this.message = message;
            this.exception = exception;
        }

        @Override
        public void run() {
            switch (state) {
                case CONNECTED:
                    try {
                        connected(channel);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel, e);
                    }
                    break;
                case DISCONNECTED:
                    try {
                        disconnected(channel);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel, e);
                    }
                    break;
                case SENT:
                    try {
                        write(channel, message);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel
                            + ", message is " + message, e);
                    }
                    break;
                case RECEIVED:
                    try {
                        reply(channel, message);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel
                            + ", message is " + message, e);
                    }
                    break;
                case CAUGHT:
                    try {
                        caught(channel, exception);
                    } catch (Exception e) {
                        log.warn("ChannelEventRunnable handle " + state + " operation error, channel is " + channel
                            + ", message is: " + message + ", exception is " + exception, e);
                    }
                    break;
                default:
                    log.warn("unknown state: " + state + ", message is " + message);
            }
        }

    }

}