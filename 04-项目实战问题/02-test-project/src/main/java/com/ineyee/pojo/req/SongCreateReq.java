package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SongCreateReq {
    // TODO: 根据接口需求在这里给属性添加相应的校验规则
    @NotBlank(message = "name 字段不能为空")
    @Schema(description = "歌曲名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "歌曲封面", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String cover;
    @NotNull(message = "singerId 字段不能为空")
    @Schema(description = "歌手 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long singerId;
}
