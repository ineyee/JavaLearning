package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/*
 单个删除：请求参数直接用对象

 url = http://localhost:9999/tp-dev/singer/remove
 body = {
    "id": 1
 }
 */
@Data
public class SingerDeleteReq {
    // @NotNull id 字段必填
    @NotNull
    private Long id;
}
