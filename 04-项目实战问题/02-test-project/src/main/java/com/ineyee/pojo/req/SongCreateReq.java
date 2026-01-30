package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SongCreateReq {
    // TODO: 根据接口需求在这里给属性添加相应的校验规则
    private String name;
    private String cover;
    @NotNull(message = "singerId 字段不能为空")
    private Long singerId;
}
