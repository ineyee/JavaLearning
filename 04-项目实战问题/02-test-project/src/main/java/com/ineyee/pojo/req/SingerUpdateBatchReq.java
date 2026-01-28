package com.ineyee.pojo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SingerUpdateBatchReq {
    @NotEmpty(message = "singerList 字段不能为空")
    private List<@Valid SingerUpdateReq> singerList;
}
