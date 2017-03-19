package com.zhaopeng.eagle.entity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhaopeng on 2016/10/31.
 */
public class RPCFuture implements Future<Object> {



    private Request request;

    private Response response;

    private Lock lock;

    private Condition condition;

    private int timeout;

    public RPCFuture(Request request) {
        lock = new ReentrantLock();
        condition = lock.newCondition();
        this.request=request;
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {

        try {
            lock.lock();
            if (response == null) {
                condition.await(timeout, TimeUnit.MILLISECONDS);
            }
            return  response!=null? response.getResult():null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public void done() {
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
