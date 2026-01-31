package com.ineyee.pojo.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SingerGetQuery {
    @NotNull(message = "id 字段不能为空")
    @Schema(description = "歌手 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
}
