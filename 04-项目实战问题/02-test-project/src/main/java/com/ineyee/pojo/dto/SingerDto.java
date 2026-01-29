package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Singer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SingerDto {
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String name;
    private Integer sex;

    public static SingerDto from(Singer singerPo) {
        SingerDto dto = new SingerDto();
        dto.setId(singerPo.getId());
        dto.setCreateTime(singerPo.getCreateTime());
        dto.setUpdateTime(singerPo.getUpdateTime());
        dto.setName(singerPo.getName());
        dto.setSex(singerPo.getSex());
        return dto;
    }
}
