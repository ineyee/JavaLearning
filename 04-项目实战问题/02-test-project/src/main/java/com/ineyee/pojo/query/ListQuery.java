package com.ineyee.pojo.query;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ListQuery {
    // 分页参数（可选）
    // 如果数据量巨大时
    // 一页一条数据，那么 pageNum 就会很大，所以用 Long
    // 一页多条数据，那么 pageSize 就会很大，所以用 Long
    @Min(1)
    private Long pageNum;
    @Min(10)
    private Long pageSize;

    // 模糊搜索参数（可选）
    private String keyword;
}
