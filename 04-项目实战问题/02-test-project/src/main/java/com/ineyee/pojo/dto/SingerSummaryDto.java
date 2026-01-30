package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Singer;
import lombok.Data;

@Data
public class SingerSummaryDto {
    private Long id;
    private String name;
    private Integer sex;

    public static SingerSummaryDto from(Singer singerPo) {
        SingerSummaryDto dto = new SingerSummaryDto();
        dto.setId(singerPo.getId());
        dto.setName(singerPo.getName());
        dto.setSex(singerPo.getSex());
        return dto;
    }
}
