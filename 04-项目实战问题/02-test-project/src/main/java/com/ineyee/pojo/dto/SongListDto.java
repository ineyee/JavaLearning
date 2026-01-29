package com.ineyee.pojo.dto;

import lombok.Data;

@Data
public class SongListDto {
    private Long songId;
    private String songName;
    private String songCover;

    private String singerName;
    private Integer singerSex;
}
