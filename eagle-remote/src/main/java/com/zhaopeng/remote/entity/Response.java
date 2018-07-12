package com.zhaopeng.remote.entity;

/**
 * Created by zhaopeng on 2016/11/4.
 */
public class Response {

    // 请求id
    private String requestId;

    // 错误代码
    private String error;

    //返回结果
    private Object result;

    // 是否心跳
    public  boolean heartEvent=false;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    public boolean isHeartEvent() {
        return heartEvent;
    }

    public void setHeartEvent(boolean heartEvent) {
        this.heartEvent = heartEvent;
    }
}
