package com.zhaopeng.remote.entity;

/**
 * Created by zhaopeng on 2016/11/4.
 */
public class Response {

    // 请求id
    private String requestId;


    private byte status = OK;


    private String errorMsg;

    //返回结果
    private Object result;

    // 是否心跳
    public  boolean heartEvent=false;


    public Response(String requestId){

        this.requestId=requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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


    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * ok.
     */
    public static final byte OK = 20;

    /**
     * clien side timeout.
     */
    public static final byte CLIENT_TIMEOUT = 30;

    /**
     * server side timeout.
     */
    public static final byte SERVER_TIMEOUT = 31;

    /**
     * request format error.
     */
    public static final byte BAD_REQUEST = 40;

    /**
     * response format error.
     */
    public static final byte BAD_RESPONSE = 50;

    /**
     * service not found.
     */
    public static final byte SERVICE_NOT_FOUND = 60;

    /**
     * service error.
     */
    public static final byte SERVICE_ERROR = 70;

    /**
     * internal server error.
     */
    public static final byte SERVER_ERROR = 80;

    /**
     * internal server error.
     */
    public static final byte CLIENT_ERROR = 90;

    /**
     * server side threadpool exhausted and quick return.
     */
    public static final byte SERVER_THREADPOOL_EXHAUSTED_ERROR = 100;
}
