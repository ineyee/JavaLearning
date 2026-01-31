package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SingerUpdateReq {
    @NotNull(message = "id 字段不能为空")
    @Schema(description = "歌手 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(description = "歌手名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;
    @Schema(description = "歌手性别", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer sex;
}

