package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Singer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SingerDetailDto {
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String name;
    private Integer sex;

    public static SingerDetailDto from(Singer singerPo) {
        SingerDetailDto dto = new SingerDetailDto();
        dto.setId(singerPo.getId());
        dto.setCreateTime(singerPo.getCreateTime());
        dto.setUpdateTime(singerPo.getUpdateTime());
        dto.setName(singerPo.getName());
        dto.setSex(singerPo.getSex());
        return dto;
    }
}
