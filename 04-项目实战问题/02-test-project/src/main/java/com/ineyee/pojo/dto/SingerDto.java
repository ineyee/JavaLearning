package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Singer;
import lombok.Data;

@Data
public class SingerDto {
    private Long id;
    private String name;
    private Integer sex;

    public static SingerDto from(Singer singerPo) {
        SingerDto dto = new SingerDto();
        dto.setId(singerPo.getId());
        dto.setName(singerPo.getName());
        dto.setSex(singerPo.getSex());
        return dto;
    }
}
