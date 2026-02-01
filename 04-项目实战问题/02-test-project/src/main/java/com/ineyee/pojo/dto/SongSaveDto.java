package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Song;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class SongSaveDto {
    @Schema(description = "歌曲 id")
    private Long id;
    @Schema(description = "歌曲创建时间")
    private LocalDateTime createTime;
    @Schema(description = "歌曲更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "歌曲名称")
    private String name;
    @Schema(description = "歌曲封面")
    private String cover;

    public static SongSaveDto from(Song songPo) {
        SongSaveDto dto = new SongSaveDto();
        BeanUtils.copyProperties(songPo, dto);
        return dto;
    }
}
