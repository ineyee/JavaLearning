package com.ineyee.pojo.dto;

import lombok.Data;

@Data
public class SingerListDto {
    private Long singerId;
    private String singerName;

    private Integer songCount;
}

// 歌手列表里只需要展示每个歌手的名称、拥有的歌曲数量
// 当然 id 是必须的
// 所以我们返回给客户端的数据结构如下：
// {
//    "code": 0,
//    "message": "Success",
//    "data": {
//        "list": [
//            {
//                "id": 123456,
//                "name": "周杰伦",
//                "songCount": 7
//            }
//        ]
//    }
// }