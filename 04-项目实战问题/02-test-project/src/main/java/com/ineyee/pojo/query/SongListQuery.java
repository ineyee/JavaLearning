package com.ineyee.pojo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SongListQuery extends ListQuery {
    // TODO: 按需追加查询参数
    private Long singerId;
}
