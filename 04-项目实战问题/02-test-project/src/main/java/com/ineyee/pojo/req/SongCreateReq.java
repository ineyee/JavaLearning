package com.ineyee.pojo.req;

import lombok.Data;

@Data
public class SongCreateReq {
    // TODO: 根据接口需求在这里给属性添加相应的校验规则
    private String name;
    private String cover;
    private Long singerId;
}
