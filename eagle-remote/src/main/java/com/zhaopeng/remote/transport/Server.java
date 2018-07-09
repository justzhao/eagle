package com.zhaopeng.remote.transport;

/**
 * Created by zhaopeng on 2018/7/9.
 */
public interface Server {

    public void doOpen() throws Throwable;

    public void doClose() throws Throwable;


    public void send(Object message, boolean sent);


    public void close();


}
