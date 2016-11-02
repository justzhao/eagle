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

    private   String result;
    private Lock lock;

    private Condition condition;
    public RPCFuture(){
        lock=new ReentrantLock();
        condition=lock.newCondition();
    }

    public RPCFuture(String result) {
        this.result = result;
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
            if(result==null){
                condition.await(1000,TimeUnit.MILLISECONDS);
            }
            return result;
        }finally {
            lock.unlock();
        }

    }
    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public  void done(){
        try {
            condition.signal();
        }finally {
            lock.unlock();
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
