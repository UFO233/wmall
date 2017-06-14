package com.wmall.exception;

import java.io.Serializable;

/**
 * Created by iven on 2016/2/17.
 */
public class BusinessException extends RuntimeException implements Serializable {

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
    }

    public BusinessException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
