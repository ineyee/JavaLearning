package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Singer;
import com.ineyee.pojo.po.Song;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SongDetailDto {
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String name;
    private String cover;

    private SingerDto singer;

    public static SongDetailDto from(Song songPo, Singer singerPo) {
        SongDetailDto dto = new SongDetailDto();
        dto.setId(songPo.getId());
        dto.setCreateTime(songPo.getCreateTime());
        dto.setUpdateTime(songPo.getUpdateTime());
        dto.setName(songPo.getName());
        dto.setCover(songPo.getCover());
        dto.setSinger(SingerDto.from(singerPo));
        return dto;
    }
}
