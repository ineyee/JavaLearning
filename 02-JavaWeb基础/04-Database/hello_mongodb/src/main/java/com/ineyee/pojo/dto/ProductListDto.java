package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
public class ProductListDto {
    @Schema(description = "产品 id")
    private String id;
    @Schema(description = "产品名称")
    private String name;
    @Schema(description = "产品价格")
    private BigDecimal price;

    public static ProductListDto from(Product productPo) {
        ProductListDto dto = new ProductListDto();
        BeanUtils.copyProperties(productPo, dto);
        return dto;
    }
}
