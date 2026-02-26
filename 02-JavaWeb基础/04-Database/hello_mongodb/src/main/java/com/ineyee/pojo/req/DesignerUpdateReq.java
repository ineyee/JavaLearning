package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DesignerUpdateReq {
    @Schema(description = "设计师年龄", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer age;
    @Schema(description = "设计师性别", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sex;
}
