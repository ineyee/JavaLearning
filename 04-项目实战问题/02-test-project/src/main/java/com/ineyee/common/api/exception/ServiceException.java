package com.ineyee.common.api.exception;

public class ServiceException extends Exception {
    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    private final Integer code;

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "code=" + code +
                ", message=" + getMessage() +
                '}';
    }
}
