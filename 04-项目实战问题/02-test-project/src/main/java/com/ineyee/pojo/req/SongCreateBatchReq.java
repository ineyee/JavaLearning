package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SongCreateBatchReq {
    @NotEmpty(message = "songList 字段不能为空")
    @Schema(description = "歌曲列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<@Valid SongCreateReq> songList;
}
