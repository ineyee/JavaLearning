package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Singer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SingerSummaryDto {
    @Schema(description = "歌手 id")
    private Long id;
    @Schema(description = "歌手名称")
    private String name;
    @Schema(description = "歌手性别")
    private Integer sex;

    public static SingerSummaryDto from(Singer singerPo) {
        SingerSummaryDto dto = new SingerSummaryDto();
        BeanUtils.copyProperties(singerPo, dto);
        return dto;
    }
}
