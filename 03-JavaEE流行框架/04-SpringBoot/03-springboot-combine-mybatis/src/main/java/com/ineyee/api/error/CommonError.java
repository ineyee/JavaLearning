package com.ineyee.api.error;

/// 通用错误码及错误信息
/// 成功码 0
/// 错误码范围 -100000 ~ -199999
public enum CommonError implements Error {
    SUCCESS(0, "Success"),
    REQUEST_ERROR(-100000, "Request Failed");

    private final Integer code;
    private final String message;

    CommonError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
