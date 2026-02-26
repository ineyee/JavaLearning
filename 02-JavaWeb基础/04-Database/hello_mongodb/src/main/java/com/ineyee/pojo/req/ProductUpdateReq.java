package com.ineyee.pojo.req;

import com.ineyee.pojo.po.Designer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/*
 单个更新：请求参数直接用对象

 url = http://localhost:9999/tp-dev/product/update
 body = {
    "id": 1,
    "price": "7777"
 }
 */
// 属性其实就是 po 里 createTime、updateTime 以外的字段
// id 字段必填，其它字段都是可选更新、那就不需要添加校验规则了
@Data
public class ProductUpdateReq {
    @NotNull(message = "id 字段不能为空")
    @Schema(description = "产品 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;
    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;
    @Schema(description = "产品描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    @Schema(description = "产品价格", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal price;
    @Schema(description = "设计师列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Designer> designerList;
}
