package com.ineyee.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDesignerCreateReq {
    @NotBlank(message = "productId 字段不能为空")
    @Schema(description = "产品 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productId;

    @NotNull(message = "designer 字段不能为空")
    @Valid
    private DesignerCreateReq designer;
}
