package com.ineyee.pojo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SongUpdateBatchReq {
    @NotEmpty(message = "songList 字段不能为空")
    private List<@Valid SongUpdateReq> songList;
}
