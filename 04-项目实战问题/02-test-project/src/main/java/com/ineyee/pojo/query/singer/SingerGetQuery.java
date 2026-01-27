package com.ineyee.pojo.query.singer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SingerGetQuery {
    @NotNull
    private Long id;
}
