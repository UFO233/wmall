package com.wmall.exception;

/**
 * Created by Iven on 2015-02-03.
 */
public class ParamValidationException extends Exception {

    public ParamValidationException(String message) {
        super(message);
    }

    public ParamValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamValidationException(Throwable cause) {
        super(cause);
    }

}
