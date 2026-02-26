package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DesignerUpdateReq {
    @NotBlank(message = "productId 字段不能为空")
    @Schema(description = "产品 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productId;

    @NotBlank(message = "designerName 字段不能为空")
    @Schema(description = "设计师名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String designerName;

    @Schema(description = "设计师年龄", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer age;

    @Schema(description = "设计师性别", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sex;
}
