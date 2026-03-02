package registerlogin.constant.response;

/// 用户模块响应
/// 错误码范围 -200000 ~ -299999
public enum UserResponse implements Response {
    USER_ALREADY_EXIST(-200000, "用户已存在"),
    EMAIL_PASSWORD_NOT_CORRECT(-200001, "邮箱或密码不正确"),
    INVALID_TOKEN(-200002, "登录凭证无效，请先登录");

    private final Integer code;
    private final String message;

    UserResponse(Integer code, String message) {
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
