package com.ineyee.pojo.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SongGetQuery {
    @NotNull(message = "id 字段不能为空")
    private Long id;
}
