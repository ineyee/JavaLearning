package com.ineyee.pojo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

// 列表查询：url = http://localhost:9999/tp-dev/product/list?pageNum=1&pageSize=10
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductListQuery extends ListQuery {
    // 可按需增加其它参数，如根据产品价格查询等
    BigDecimal minPrice;
    BigDecimal maxPrice;
}
