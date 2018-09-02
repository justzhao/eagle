package com.zhaopeng.common.exception;

/**
 * @author zhaopeng
 * @date 2018/09/02
 */
public class DispatchException extends Exception {


    public DispatchException() {
        super();
    }


    public DispatchException(String message) {
        super(message);
    }


    public DispatchException(String message, Throwable cause) {
        super(message, cause);
    }


    public DispatchException(Throwable cause) {
        super(cause);
    }
}
