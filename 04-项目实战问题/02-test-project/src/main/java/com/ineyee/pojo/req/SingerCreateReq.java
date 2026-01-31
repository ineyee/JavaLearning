package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SingerCreateReq {
    @NotNull(message = "name 字段不能为空")
    @Schema(description = "歌手名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "歌手性别", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sex;
}
