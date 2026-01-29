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
// 歌手详情里需要展示歌手的东西比较多，所以我们尽可能多的返回歌手字段
// 当然除了那些敏感字段
// 所以我们返回给客户端的数据结构如下：
// {
//    "code": 0,
//    "message": "Success",
//    "data": {
//        "id": 4,
//        "createTime": "2022-08-29T21:19:25",
//        "updateTime": "2017-05-20T02:02:37",
//        "name": "侯云熙",
//        "sex": 0
//    }
// }
