package com.ineyee.pojo.query;

import lombok.Data;

@Data
public class ListQuery {
    // 分页参数（可选）
    // 如果数据量巨大时
    // 一页一条数据，那么 pageNum 就会很大，所以用 Long
    // 一页多条数据，那么 pageSize 就会很大，所以用 Long
    private Long pageNum;
    private Long pageSize;

    public Long getPageNum() {
        if (pageNum == null) {
            // null 是有意义的，代表不搞分页
            return null;
        }

        // 最小是 1，上不封顶
        return Math.max(pageNum, 1L);
    }

    public Long getPageSize() {
        if (pageSize == null) {
            // null 是有意义的，代表不搞分页
            return null;
        }

        // 最小是 10，上不封顶
        return Math.max(pageSize, 10L);
    }

    // 模糊搜索参数（可选）
    private String keyword;
}
