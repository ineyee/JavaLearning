package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/*
 单个更新：请求参数直接用对象

 url = http://localhost:9999/tp-dev/singer/update
 body = {
    "id": 1,
    "name": "Curry"
 }
 */
@Data
public class SingerUpdateReq {
    // @NotNull id 字段必填
    @NotNull
    private Long id;

    private String name;
    private String sex;
}
