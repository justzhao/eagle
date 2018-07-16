package com.zhaopeng.remote.hanlder;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.exception.RemotingException;
import com.zhaopeng.remote.dispacher.ChannelEventRunnable;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.entity.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.*;

/**
 * Created by zhaopeng on 2018/7/15.
 */
public class NettyChannelHandler {


    ExecutorService executor;

    ChannelHandler channelHandler;


    public NettyChannelHandler(Url url) {
        executor = new ThreadPoolExecutor(Constants.THREAD_POOL_CORE_SIZE, url.getThreads(), Constants.THREAD_POOL_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(Constants.THREAD_POOL_QUEUE_SIZE));


    }

    /**
     * @param channel
     * @param message
     * @throws RemotingException
     */
    public void received(Channel channel, Object message) throws RemotingException {

        try {
            executor.execute(new ChannelEventRunnable(channel, ChannelEventRunnable.ChannelState.RECEIVED, message));
        } catch (Throwable t) {
            //TODO A temporary solution to the problem that the exception information can not be sent to the opposite end after the thread pool is full. Need a refactoring
            //fix The thread pool is full, refuses to call, does not return, and causes the consumer to wait for time out
            if (message instanceof Request && t instanceof RejectedExecutionException) {
                Request request = (Request) message;
                if (request.isTwoWay()) {
                    String msg = "Server side threadpool is exhausted ,detail msg:" + t.getMessage();
                    Response response = new Response(request.getRequestId());
                    response.setStatus(Response.SERVER_THREADPOOL_EXHAUSTED_ERROR);
                    response.setErrorMsg(msg);
                    channel.writeAndFlush(response);
                    return;
                }
            }

        }


    }


    public void write(ChannelHandlerContext ctx, Object msg) throws Exception {


        channelHandler.sent(ctx.channel(), msg);
    }

}
