package com.ineyee.pojo.req;

import com.ineyee.pojo.po.Designer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

/*
 单个保存：请求参数直接用对象

 url = http://localhost:9999/tp-dev/product/save
 body = {
    "name": "iPhoneX",
    "description": "首款全面屏",
    "price": 8888
 }
 */
// 属性其实就是 po 里 id、createTime、updateTime 以外的字段
// 只是我们需要根据接口需求在这里给属性添加相应的校验规则
@Data
public class ProductCreateReq {
    // @NotBlank = 不能为 null + 字符串不能为空串 + 字符串不能全是空格字符
    @NotBlank(message = "name 字段不能为空")
    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Length(min = 0, max = 10, message = "description 字段长度必须在 0 ~ 10 之间")
    @Schema(description = "产品描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    @NotNull(message = "price 字段不能为空")
    @Schema(description = "产品价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;
    @Schema(description = "设计师列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Designer> designerList;
}
