package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/*
 单个删除：请求参数直接用对象

 url = http://localhost:9999/tp-dev/product/remove
 body = {
    "id": 1
 }
 */
@Data
public class ProductDeleteReq {
    @NotNull(message = "id 字段不能为空")
    private Long id;
}
