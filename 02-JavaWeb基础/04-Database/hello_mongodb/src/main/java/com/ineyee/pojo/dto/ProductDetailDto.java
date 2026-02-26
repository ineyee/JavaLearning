package com.ineyee.pojo.dto;

import com.ineyee.pojo.po.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDetailDto {
    @Schema(description = "产品 id")
    private String id;
    @Schema(description = "产品创建时间")
    private LocalDateTime createTime;
    @Schema(description = "产品更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "产品名称")
    private String name;
    @Schema(description = "产品描述")
    private String desc;
    @Schema(description = "产品价格")
    private BigDecimal price;

    public static ProductDetailDto from(Product productPo) {
        ProductDetailDto dto = new ProductDetailDto();
        BeanUtils.copyProperties(productPo, dto);
        return dto;
    }
}
