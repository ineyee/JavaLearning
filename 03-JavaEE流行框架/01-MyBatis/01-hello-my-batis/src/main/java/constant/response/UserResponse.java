package constant.response;

/// 用户模块响应
/// 错误码范围 -200000 ~ -299999
public enum UserResponse implements Response {
    USER_ALREADY_EXIST(-200000, "用户已存在"),
    USER_NOT_EXIST(-200001, "用户不存在"),
    CANT_BATCH_UPDATE_EMAIL(-200002, "不能批量修改邮箱"),
    EMAIL_ALREADY_EXIST(-200003, "邮箱已存在");

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
