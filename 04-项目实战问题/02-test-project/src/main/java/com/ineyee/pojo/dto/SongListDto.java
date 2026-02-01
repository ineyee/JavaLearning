package com.ineyee.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SongListDto {
    @Schema(description = "歌曲 id")
    private Long songId;
    @Schema(description = "歌曲名称")
    private String songName;
    @Schema(description = "歌曲封面")
    private String songCover;

    @Schema(description = "歌手名称")
    private String singerName;
}
