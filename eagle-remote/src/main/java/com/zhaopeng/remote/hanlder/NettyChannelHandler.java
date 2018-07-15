package com.zhaopeng.remote.hanlder;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.exception.RemotingException;
import io.netty.channel.Channel;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaopeng on 2018/7/15.
 */
public class NettyChannelHandler {


    ExecutorService executor;

    ChannelHandler channelHandler;


    public NettyChannelHandler(Url url){
        executor = new ThreadPoolExecutor(Constants.THREAD_POOL_CORE_SIZE, url.getThreads(), Constants.THREAD_POOL_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(Constants.THREAD_POOL_QUEUE_SIZE));


    }

    /**
     *
     * @param channel
     * @param message
     * @throws RemotingException
     */
   public void received(Channel channel, Object message) throws RemotingException{




   }


}
