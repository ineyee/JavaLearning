package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/*
 单个更新：请求参数直接用对象

 url = http://localhost:9999/tp-dev/product/update
 body = {
    "id": 1,
    "price": "7777"
 }
 */
@Data
public class ProductUpdateReq {
    @NotNull(message = "id 字段不能为空")
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}
