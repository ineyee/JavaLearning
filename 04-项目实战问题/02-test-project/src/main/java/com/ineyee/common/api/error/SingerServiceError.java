package com.ineyee.common.api.error;

/// 用户模块错误码及错误信息
/// 错误码范围 -200000 ~ -299999
public enum SingerServiceError implements ServiceError {
    SINGER_NOT_EXIST(-200000, "歌手不存在");

    SingerServiceError(Integer code, String message) {
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

