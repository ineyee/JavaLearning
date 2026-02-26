package com.ineyee.pojo.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 这个 po 没有对应的集合，因为它是别人的嵌套 po
@Data
public class Designer {
    @NotBlank(message = "name 字段不能为空")
    @Schema(description = "设计师名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "设计师年龄", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer age;
    @Schema(description = "设计师性别", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sex;
}