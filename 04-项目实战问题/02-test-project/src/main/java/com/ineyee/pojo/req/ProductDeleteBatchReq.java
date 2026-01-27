package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/*
 批量删除：请求参数最好包一层对象，而不是直接用数组

 url = http://localhost:9999/tp-dev/product/removeBatch
 body = {
    "idList": [1, 2, 3]
 }
 */
@Data
public class ProductDeleteBatchReq {
    @NotEmpty(message = "idList 字段不能为空")
    private List<@NotNull Long> idList;
}
