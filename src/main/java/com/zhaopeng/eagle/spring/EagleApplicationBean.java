package com.zhaopeng.eagle.spring;

import com.zhaopeng.eagle.util.AsyncTaskSubmitUtil;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by zhaopeng on 2016/11/19.
 */
public class EagleApplicationBean implements DisposableBean {

    private int port;

    private String protocol;

    private int threads;

    private int accepts;

    private ExecutorService executor;

    public void init() {
        executor = new ThreadPoolExecutor(threads, threads, 600l, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(65536));

        AsyncTaskSubmitUtil taskSubmitUtil=   AsyncTaskSubmitUtil.newThreadPoolInstance();
        taskSubmitUtil.setExecutorService(executor);
      //  ServiceFactory.getInstance().getSetsMap().put("executor",executor);
       // ServiceFactory.getInstance().getSetsMap().put("accepts",accepts);
       // ServiceFactory instance = ServiceFactory.getInstance();
       // instance.serverStart();
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getAccepts() {
        return accepts;
    }

    public void setAccepts(int accepts) {
        this.accepts = accepts;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
    @Override
    public void destroy() throws Exception {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
