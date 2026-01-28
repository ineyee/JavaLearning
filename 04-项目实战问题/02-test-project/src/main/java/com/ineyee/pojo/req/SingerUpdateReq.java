package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SingerUpdateReq {
    @NotNull(message = "id 字段不能为空")
    private Long id;
    private String name;
    private Integer sex;
}

