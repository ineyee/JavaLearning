package com.ineyee.common.api.error;

/// 用户模块错误码及错误信息
/// 错误码范围 -200000 ~ -299999
public enum UserServiceError implements ServiceError {
    USER_ALREADY_EXIST(-200000, "User Already Exist"),
    USER_NOT_EXIST(-200001, "User Not Exist"),
    CANT_BATCH_UPDATE_EMAIL(-200002, "Cant Batch Update Email"),
    EMAIL_ALREADY_EXIST(-200003, "Email Already Exist");

    UserServiceError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

