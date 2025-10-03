package constant.response;

/// 产品模块响应
/// 错误码范围 -300000 ~ -399999
public enum ProductResponse implements Response {
    PRODUCT_NOT_EXIST(-300001, "产品不存在");

    private final Integer code;
    private final String message;

    ProductResponse(Integer code, String message) {
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
