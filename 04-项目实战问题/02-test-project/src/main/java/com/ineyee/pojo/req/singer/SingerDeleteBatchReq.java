package com.ineyee.pojo.req.singer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/*
 批量删除：请求参数最好包一层对象，而不是直接用数组

 url = http://localhost:9999/tp-dev/singer/removeBatch
 body = {
    "idList": [1, 2, 3]
 }
 */
@Data
public class SingerDeleteBatchReq {
    // @NotEmpty 保证至少有一条，不是 null、不是 []
    // @NotNull 数组里的 id 不能是 null
    @NotEmpty
    private List<@NotNull Long> idList;
}
