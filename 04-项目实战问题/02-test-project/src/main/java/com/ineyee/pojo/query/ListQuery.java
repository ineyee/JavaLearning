package com.ineyee.pojo.query;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ListQuery {
    // 分页参数（可选）
    // 如果数据量巨大时：一页一条数据，那么 pageNum 就会很大，所以用 Long；一页多条数据，那么 pageSize 就会很大，所以用 Long
    @Schema(description = "当前第几页（传了就是分页查询，不传就是全部查询）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long pageNum;
    @Schema(description = "一页多少条数据（传了就是分页查询，不传就是全部查询）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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

    // 计算 offset（自定义 SQL 实现时可能需要）
    @Hidden
    public Long getOffset() {
        if (pageNum == null || pageSize == null) {
            return null;
        }
        return (pageNum - 1) * pageSize;
    }

    // 模糊搜索参数（可选）
    @Schema(description = "模糊搜索关键字（传了就会模糊所搜，不传就不会模糊搜索）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String keyword;
}
