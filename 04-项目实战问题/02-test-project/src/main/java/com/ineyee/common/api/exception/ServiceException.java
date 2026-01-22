package com.ineyee.common.api.exception;

import com.ineyee.common.api.error.ServiceError;

public class ServiceException extends Exception {
    public ServiceException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.code = serviceError.getCode();
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
