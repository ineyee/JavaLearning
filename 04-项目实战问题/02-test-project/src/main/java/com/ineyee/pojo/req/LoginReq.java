package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {
    @NotBlank(message = "email 字段不能为空")
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @NotBlank(message = "password 字段不能为空")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
