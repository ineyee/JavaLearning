package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SongUpdateReq {
    @NotNull(message = "id 字段不能为空")
    private Long id;
    private String name;
    private String cover;
    private Long singerId;
}

