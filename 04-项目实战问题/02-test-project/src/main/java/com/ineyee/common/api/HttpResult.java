package com.ineyee.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ineyee.common.api.error.CommonServiceError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HttpResult<T> {
    /**
     * 用于响应成功时有数据 {"code": 0, "message": "Success", "data": xxx}
     */
    public static <T> HttpResult<T> ok(T data) {
        return new HttpResult<>(CommonServiceError.SUCCESS.getCode(), CommonServiceError.SUCCESS.getMessage(), data);
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
     * 用于响应错误时业务异常以外的系统异常 {"code": -100000, "message": xxx}
     * 我们把系统异常的错误码统一为 -100000，错误信息自动获取
     */
    public static <T> HttpResult<T> error(String msg) {
        return error(CommonServiceError.REQUEST_ERROR.getCode(), msg);
    }

    private HttpResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @NotNull(message = "错误码不能为空")
    @Schema(description = "错误码，0 代表成功，非 0 代表失败", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;
    @NotNull(message = "错误信息不能为空")
    @Schema(description = "错误信息，成功时为 Success，失败时为相应的 msg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)  // null 时不序列化
    @Schema(description = "响应有数据时才返回该字段，响应仅状态时不返回该字段", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private T data;
}
