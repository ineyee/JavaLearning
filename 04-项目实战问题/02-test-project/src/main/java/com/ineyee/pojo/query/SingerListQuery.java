package com.ineyee.pojo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 列表查询：url = http://localhost:9999/tp-dev/singer/list?pageNum=1&pageSize=10
@EqualsAndHashCode(callSuper = true)
@Data
public class SingerListQuery extends ListQuery {
    // 可按需增加其它参数，如根据性别查询、根据起始日期和结束日期查询等
    // ......
}
