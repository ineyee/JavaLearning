package com.ineyee.pojo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/*
 批量保存：请求参数最好包一层对象，而不是直接用数组

 url = http://localhost:9999/tp-dev/product/save
 body = {
    "productList": [
        { "name": "iPhoneX", "description": "首款全面屏", "price": 8888 },
        { "name": "华为 Mate30 Pro", "description": "国货之光", "price":  6666.66 },
        { "name": "小米 17", "description": "注意小字", "price":  4444.44 },
    ]
 }
 */
@Data
public class ProductCreateBatchReq {
    // @NotEmpty = 不能为 null + 字符串不能为空串、集合里不能没有元素
    // @Valid 触发内层参数校验
    @NotEmpty(message = "productList 字段不能为空")
    private List<@Valid ProductCreateReq> productList;
}
