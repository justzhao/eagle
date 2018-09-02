package com.zhaopeng.remote;

import java.io.Serializable;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */
@Slf4j
@Data
public class MessageWrapper implements Serializable {


    private MessageProtocol protocol;
    private String sessionId;
    private Object body;

    private MessageWrapper() {
    }

    public MessageWrapper(MessageProtocol protocol, String sessionId, Object body) {
        this.protocol = protocol;
        this.sessionId = sessionId;
        this.body = body;
    }

    public enum MessageProtocol {
        CONNECT, CLOSE, HEART_BEAT, SEND, RECEIVE, NOTIFY, REPLY, NO_CONNECT
    }


    public boolean isConnect() {
        return MessageProtocol.CONNECT.equals(this.protocol);
    }

    public boolean isClose() {
        return MessageProtocol.CLOSE.equals(this.protocol);
    }

    public boolean isHeartbeat() {
        return MessageProtocol.HEART_BEAT.equals(this.protocol);
    }

    public boolean isSend() {
        return MessageProtocol.SEND.equals(this.protocol);
    }

    public boolean isNotify() {
        return MessageProtocol.NOTIFY.equals(this.protocol);
    }

    public boolean isReply() {
        return MessageProtocol.REPLY.equals(this.protocol);
    }

    public boolean isNoKeepAliveMessage() {
        return MessageProtocol.NO_CONNECT.equals(this.protocol);
    }

}
