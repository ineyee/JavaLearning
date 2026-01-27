package com.ineyee.pojo.req.singer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/*
 批量更新：请求参数最好包一层对象，而不是直接用数组

 url = http://localhost:9999/tp-dev/singer/updateBatch
 body = {
    "singerList": [
        { "id": 1, "name": "三三" },
        { "id": 2, "sex": "男" },
        { "id": 3, "name": "五五", "sex": "女" }
    ]
 }
 */
@Data
public class SingerUpdateBatchReq {
    // @NotEmpty 保证至少有一条，不是 null、不是 []
    // @Valid 触发内部 id 必填校验
    @NotEmpty
    private List<@Valid SingerUpdateReq> singerList;
}
