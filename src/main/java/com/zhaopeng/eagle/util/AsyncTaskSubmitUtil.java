package com.zhaopeng.eagle.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaopeng on 2016/11/15.
 */
public class AsyncTaskSubmitUtil {

    private static class SingletonHolder {
        private static final ThreadPoolExecutor INSTANCE = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
    }

    private  static ThreadPoolExecutor newThreadPoolInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 提交任务
     * @param task
     */
    public static void submit(Runnable task){
        ThreadPoolExecutor threadPoolExecutor=newThreadPoolInstance();
        threadPoolExecutor.submit(task);
    }
}
