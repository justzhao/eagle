package com.zhaopeng.common.exception;

/**
 * @author zhaopeng
 * @date 2018/09/01
 */
public class LostConnectException extends RuntimeException {

    public LostConnectException(){
        super();
    }

    public LostConnectException(String message) {
        super(message);
    }

    public LostConnectException(Throwable throwable) {
        super(throwable);
    }

    public LostConnectException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
