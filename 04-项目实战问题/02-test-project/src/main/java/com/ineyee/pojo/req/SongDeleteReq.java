package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "删除歌曲请求")
public class SongDeleteReq {
    @NotNull(message = "id 字段不能为空")
    @Schema(description = "歌曲 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
}
