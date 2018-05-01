package com.troy.keeper.core.error;

/**
 * Performance Check Exception for a method which execute a long time beyond our expectation
 * User: Harry
 * Date: 17-6-29
 * Time: 下午4:24
 */
public class PerformanceCheckException extends KeeperException {

    public PerformanceCheckException(int code, String msg) {
        super(code, msg);
    }

    public PerformanceCheckException(String s) {
        super(s);
    }

    public PerformanceCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public PerformanceCheckException(Throwable cause) {
        super(cause);
    }
}
