package com.ineyee.common.api.error;

/// 产品模块错误码及错误信息
/// 错误码范围 -300000 ~ -399999
public enum ProductServiceError implements ServiceError {
    PRODUCT_NOT_EXIST(-300000, "产品不存在"),
    DESIGNER_NOT_EXIST(-300001, "设计师不存在");

    ProductServiceError(Integer code, String message) {
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

