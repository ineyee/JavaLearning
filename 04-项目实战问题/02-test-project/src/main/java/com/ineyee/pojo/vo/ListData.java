package com.ineyee.pojo.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

// 专门用来组装列表查询结果，返回给客户端
@Data
public class ListData<T> {
    /**
     * 分页列表
     */
    public static <T> ListData<T> fromPage(Page<T> queryedPage) {
        return new ListData<>(queryedPage.getRecords(), queryedPage.getCurrent(), queryedPage.getSize(), queryedPage.getTotal(), queryedPage.getPages());
    }

    /**
     * 非分页列表
     */
    public static <T> ListData<T> fromList(List<T> list) {
        return new ListData<>(list, null, null, null, null);
    }

    private ListData(List<T> list, Long pageNum, Long pageSize, Long total, Long totalPages) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPages = totalPages;
    }

    /**
     * pojo 数据列表
     * ① [pojo] 代表本次查询有数据
     * ② [] 代表本次查询没有数据
     */
    private List<T> list;

    /**
     * 当前第几页
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)  // null 时不序列化
    private Long pageNum;

    /**
     * 一页多少条
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long pageSize;

    /**
     * 数据总条数
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    /**
     * 数据总页数
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalPages;
}
