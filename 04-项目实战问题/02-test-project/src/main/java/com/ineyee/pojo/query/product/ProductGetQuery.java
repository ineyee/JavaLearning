package com.ineyee.pojo.query.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 单个查询：url = http://localhost:9999/tp-dev/product/get?id=1
@Data
public class ProductGetQuery {
    // @NotNull = 不能为 null
    @NotNull(message = "id 字段不能为空")
    private Long id;
}
