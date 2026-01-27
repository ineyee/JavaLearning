package com.ineyee.pojo.req.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/*
 单个保存：请求参数直接用对象

 url = http://localhost:9999/tp-dev/product/save
 body = {
    "name": "iPhoneX",
    "description": "首款全面屏",
    "price": 8888
 }
 */
@Data
public class ProductCreateReq {
    // @NotBlank = 不能为 null + 字符串不能为空串 + 字符串不能全是空格字符
    @NotBlank(message = "name 字段不能为空")
    private String name;
    @Length(min = 0, max = 10, message = "description 字段长度必须在 0 ~ 10 之间")
    private String description;
    @NotNull(message = "price 字段不能为空")
    private BigDecimal price;
}
