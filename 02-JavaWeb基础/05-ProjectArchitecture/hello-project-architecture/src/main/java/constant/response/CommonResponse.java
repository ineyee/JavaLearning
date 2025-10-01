package constant.response;

/// 通用响应
/// 成功码 0
/// 错误码范围 -100000 ~ -199999
public enum CommonResponse implements Response {
    SUCCESS(0, "请求成功"),
    REQUEST_ERROR(-100000, "请求失败"),
    PARAM_ERROR(-100001, "参数错误");

    private final Integer code;
    private final String message;

    CommonResponse(Integer code, String message) {
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
