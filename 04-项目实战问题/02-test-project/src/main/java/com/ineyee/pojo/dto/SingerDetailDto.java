package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Singer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class SingerDetailDto {
    @Schema(description = "歌手 id")
    private Long id;
    @Schema(description = "歌手创建时间")
    private LocalDateTime createTime;
    @Schema(description = "歌手更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "歌手名称")
    private String name;
    @Schema(description = "歌手性别")
    private Integer sex;

    public static SingerDetailDto from(Singer singerPo) {
        SingerDetailDto dto = new SingerDetailDto();
        BeanUtils.copyProperties(singerPo, dto);
        return dto;
    }
}

// 详情的字段应该尽可能多——为了全，也就是从 po 里过滤掉敏感的
//
// 歌手详情界面需要展示的歌手信息比较多，所以我们应该尽可能多的返回歌手的字段
// 但是因为歌手表是主表，歌曲表是从表，所以进了歌手详情界面后，如果想获取歌曲列表，那就单独走一下“获取歌曲列表的接口”即可，歌手详情里最好不要携带一大坨的歌曲列表
//
// 所以我们返回给客户端的数据结构如下：
// {
//     "id": 123456,
//     "createTime": "2023-05-28T16:40:12",
//     "updateTime": "2006-11-30T16:39:12",
//     "name": "周杰伦",
//     "sex": 1
// }