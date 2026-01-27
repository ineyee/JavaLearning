package com.ineyee.pojo.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SingerGetQuery {
    @NotNull
    private Long id;
}
