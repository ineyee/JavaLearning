package com.ineyee.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginDto {
    @Schema(description = "token")
    private String token;

    @Schema(description = "用户邮箱")
    private String email;
}
