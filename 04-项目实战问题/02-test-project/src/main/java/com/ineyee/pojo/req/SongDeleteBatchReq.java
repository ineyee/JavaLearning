package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SongDeleteBatchReq {
    @NotEmpty(message = "idList 字段不能为空")
    @Schema(description = "歌曲 id 列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<@NotNull Long> idList;
}
