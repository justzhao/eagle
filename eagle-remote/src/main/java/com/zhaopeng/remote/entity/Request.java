package com.zhaopeng.remote.entity;

import java.io.Serializable;

import com.zhaopeng.remote.MessageWrapper;

import lombok.Data;

/**
 * Created by zhaopeng on 2016/11/4.
 */
@Data
public class Request implements Serializable {

    /**
     * 请求id
     */
    private String requestId;

    private String sessionId;

    private MessageWrapper.MessageProtocol protocol;

    /**
     * 请求数据
     */
    // 类名字
    private String className;
    // 方法名字
    private String methodName;
    // 参数类型
    private Class<?>[] parameterTypes;
    // 参数
    private Object[] parameters;

    private Object body;

    private String version;

    public boolean heartEvent = false;

    public boolean twoWay = true;


    public Request(){

    }


    public Request(MessageWrapper.MessageProtocol protocol, String sessionId, Object body) {
        this.protocol = protocol;
        this.sessionId = sessionId;
        this.body = body;
    }

    public enum MessageProtocol {
        CONNECT, CLOSE, HEART_BEAT, SEND, RECEIVE, NOTIFY, REPLY, NO_CONNECT
    }


    public boolean isConnect() {
        return Request.MessageProtocol.CONNECT.equals(this.protocol);
    }

    public boolean isClose() {
        return Request.MessageProtocol.CLOSE.equals(this.protocol);
    }

    public boolean isHeartbeat() {
        return Request.MessageProtocol.HEART_BEAT.equals(this.protocol);
    }

    public boolean isSend() {
        return Request.MessageProtocol.SEND.equals(this.protocol);
    }

    public boolean isNotify() {
        return Request.MessageProtocol.NOTIFY.equals(this.protocol);
    }

    public boolean isReply() {
        return Request.MessageProtocol.REPLY.equals(this.protocol);
    }

    public boolean isNoKeepAliveMessage() {
        return Request.MessageProtocol.NO_CONNECT.equals(this.protocol);
    }
}
