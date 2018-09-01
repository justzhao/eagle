package com.zhaopeng.common.exception;

/**
 * @author zhaopeng
 * @date 2018/09/01
 */
public class PushException extends RuntimeException {

    public PushException() {
        super();
    }

    public PushException(String message) {
        super(message);
    }

    public PushException(Throwable throwable) {
        super(throwable);
    }

    public PushException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
