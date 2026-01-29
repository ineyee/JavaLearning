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

// 歌手详情里需要比较多的歌手信息，所以我们尽可能多的返回歌手的字段
// 但是因为歌手表是主表，歌曲表是从表，所以进了歌手详情界面后，如果想获取歌曲列表，那就单独走获取歌曲列表接口获取即可，歌手详情里最好不要携带一大坨的歌曲列表
// 所以我们返回给客户端的数据结构如下：
// {
//    "code": 0,
//    "message": "Success",
//    "data": {
//        "id": 123456,
//        "createTime": "2023-05-28T16:40:12",
//        "updateTime": "2006-11-30T16:39:12",
//        "name": "周杰伦",
//        "sex": 1
//    }
// }
