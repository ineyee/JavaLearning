package com.ineyee.pojo.req;

import com.ineyee.pojo.po.Designer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductUpdateDesignerReq {
    @NotBlank(message = "productId 字段不能为空")
    @Schema(description = "产品 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productId;

    @NotBlank(message = "designerName 字段不能为空")
    @Schema(description = "设计师名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String designerName;

    @NotNull(message = "designer 字段不能为空")
    @Valid
    private Designer designer;
}
