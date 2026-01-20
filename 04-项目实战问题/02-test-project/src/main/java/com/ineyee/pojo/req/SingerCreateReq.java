package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/*
 单个保存：请求参数直接用对象

 url = http://localhost:9999/tp-dev/singer/save
 body = {
    "name": "库里",
    "sex": "男"
 }
 */
@Data
public class SingerCreateReq {
    // @NotNull name 字段必填
    @NotNull
    private String name;
    private String sex;
}
