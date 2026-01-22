package com.ineyee.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ineyee.common.api.error.CommonError;
import lombok.Data;

@Data
public class HttpResult<T> {
    /**
     * 用于响应成功时有数据 {"code": 0, "message": "Success", "data": xxx}
     */
    public static <T> HttpResult<T> ok(T data) {
        return new HttpResult<>(CommonError.SUCCESS.getCode(), CommonError.SUCCESS.getMessage(), data);
    }

    /**
     * 用于响应成功时仅状态 {"code": 0, "message": "success"}
     */
    public static <T> HttpResult<T> ok() {
        return ok(null);
    }

    /**
     * 用于响应错误时的业务异常 {"code": xxx, "message": xxx}
     * 错误码和错误信息由业务方决定
     */
    public static <T> HttpResult<T> error(Integer code, String msg) {
        return new HttpResult<>(code, msg, null);
    }

    /**
     * 用于响应错误时业务异常以外的系统异常 {"code": -100000, "message": "Request Failed"}
     * 我们把系统异常的错误码统一为 -100000，错误信息统一为"Request Failed"
     */
    public static <T> HttpResult<T> error() {
        return error(CommonError.REQUEST_ERROR.getCode(), CommonError.REQUEST_ERROR.getMessage());
    }

    private HttpResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Integer code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)  // null 时不序列化
    private T data;
}
