package com.zhaopeng.remote.session.tcp.connector;

import com.zhaopeng.common.exception.LostConnectException;
import com.zhaopeng.common.exception.PushException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */
@Slf4j
public class NoKeepAliveTcpConnection<T> {

    private ChannelHandlerContext cxt;

    public NoKeepAliveTcpConnection(ChannelHandlerContext cxt) {
        this.cxt = cxt;
    }

    public void close() {
        cxt.close();
        log.info("the connection have been destroyed!");
    }

    public void send(T message) {
        sendMessage(message);
    }

    private void sendMessage(T message) {
        pushMessage(message);
    }

    private void pushMessage(T message) {
        boolean success = true;
        boolean sent = true;
        int timeout = 60;
        try {
            ChannelFuture cf = cxt.write(message);
            cxt.flush();
            if (sent) {
                success = cf.await(timeout);
            }
            if (cf.isSuccess()) {
                log.info("send success.");
            }
            Throwable cause = cf.cause();
            if (cause != null) {
                throw new PushException(cause);
            }
        } catch (LostConnectException e) {
            log.error("NoKeepAliveTcpConnection pushMessage occur LostConnectException.", e);
            throw new PushException(e);
        } catch (Exception e) {
            log.error("NoKeepAliveTcpConnection pushMessage occur Exception.", e);
            throw new PushException(e);
        } catch (Throwable e) {
            log.error("NoKeepAliveTcpConnection pushMessage occur Throwable.", e);
            throw new PushException("Failed to send message, cause: " + e.getMessage(), e);
        }
        if (!success) {
            throw new PushException("Failed to send message, in timeout(" + timeout + "ms) limit");
        }
    }
}
