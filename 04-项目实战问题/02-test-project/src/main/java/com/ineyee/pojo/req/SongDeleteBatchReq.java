package com.ineyee.pojo.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SongDeleteBatchReq {
    @NotEmpty(message = "idList 字段不能为空")
    private List<@NotNull Long> idList;
}
