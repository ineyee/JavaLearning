package com.ineyee.pojo.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

// 列表查询：url = http://localhost:9999/tp-dev/product/list?pageNum=1&pageSize=10
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductListQuery extends ListQuery {
    // 可按需增加其它参数，如根据产品价格查询等
    @Schema(description = "最低价格（传了就会按价格查询，不传就不会按价格查询）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    BigDecimal minPrice;
    @Schema(description = "最高价格（传了就会按价格查询，不传就不会按价格查询）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    BigDecimal maxPrice;
}
