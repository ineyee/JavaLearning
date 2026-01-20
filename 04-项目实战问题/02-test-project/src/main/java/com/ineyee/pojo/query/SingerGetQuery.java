package com.ineyee.pojo.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 单个查询：url = http://localhost:9999/tp-dev/singer/get?id=1
@Data
public class SingerGetQuery {
    // @NotNull id 字段必填
    @NotNull
    private Long id;
}
