package com.zhaopeng.eagle.util;

import java.util.concurrent.ExecutorService;

/**
 * Created by zhaopeng on 2016/11/15.
 */
public class AsyncTaskSubmitUtil {

    private static ExecutorService executorService;

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static void setExecutorService(ExecutorService executorService) {
        AsyncTaskSubmitUtil.executorService = executorService;
    }

    private static class SingletonHolder {
        private static final AsyncTaskSubmitUtil INSTANCE = new AsyncTaskSubmitUtil();
        //(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));


    }

    public static AsyncTaskSubmitUtil newThreadPoolInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 提交任务
     *
     * @param task
     */
    public static void submit(Runnable task) {

        if (executorService == null) return;
        executorService.submit(task);
        // ThreadPoolExecutor threadPoolExecutor=newThreadPoolInstance();
        //  threadPoolExecutor.submit(task);
    }
}
