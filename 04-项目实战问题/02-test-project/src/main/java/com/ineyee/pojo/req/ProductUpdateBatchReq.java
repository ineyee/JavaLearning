package com.ineyee.pojo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/*
 批量更新：请求参数最好包一层对象，而不是直接用数组

 url = http://localhost:9999/tp-dev/product/updateBatch
 body = {
    "productList": [
        { "id": 1, "price": "7777" },
        { "id": 2, "name": "HuaWei Mate30 Pro" },
        { "id": 3, "description": "营销大师" }
    ]
 }
 */
@Data
public class ProductUpdateBatchReq {
    @NotEmpty(message = "productList 字段不能为空")
    private List<@Valid ProductUpdateReq> productList;
}
