package com.ineyee.pojo.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SongListQuery extends ListQuery {
    // TODO: 按需追加查询参数
    @Schema(description = "歌手 id（传了就是获取某个歌手的歌曲列表，不传就是获取全部歌曲列表）")
    private Long singerId;
}
