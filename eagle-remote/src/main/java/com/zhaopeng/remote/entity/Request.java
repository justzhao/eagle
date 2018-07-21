package com.zhaopeng.remote.entity;

/**
 * Created by zhaopeng on 2016/11/4.
 */
public class Request {

    // 请求id
    private String requestId;
    // 类名字
    private Object data;

    private String version;


    public boolean heartEvent = false;

    public boolean twoWay = true;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isHeartEvent() {
        return heartEvent;
    }

    public void setHeartEvent(boolean heartEvent) {
        this.heartEvent = heartEvent;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }
}
