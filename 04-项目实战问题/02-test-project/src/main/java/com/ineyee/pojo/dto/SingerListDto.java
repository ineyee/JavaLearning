package com.ineyee.pojo.dto;

import lombok.Data;

@Data
public class SingerListDto {
    private Long singerId;
    private String singerName;

    private Integer songCount;
}

// 列表的字段应该尽可能少——为了快，也就是从 po 里筛选出需要的
//
// 歌手列表里只需要展示每个歌手的名称、拥有的歌曲数量（这就是一个特殊场景需要读取从表的数据）
// 当然 id 是必须的
//
// 所以我们返回给客户端的数据结构如下：
// {
//     "singerId": 123456,
//     "singerName": "周杰伦",
//     "songCount": 7
// }