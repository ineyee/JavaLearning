package com.ineyee.pojo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/*
 批量保存：请求参数最好包一层对象，而不是直接用数组

 url = http://localhost:9999/tp-dev/singer/save
 body = {
    "singerList": [
        { "name": "三三", "sex": "女" },
        { "name": "四四", "sex": "男" },
        { "name": "五五" }
    ]
 }
 */
@Data
public class SingerCreateBatchReq {
    // @NotEmpty 保证至少有一条，不是 null、不是 []
    // @Valid 触发内部 name 必填校验
    @NotEmpty
    private List<@Valid SingerCreateReq> singerList;
}
