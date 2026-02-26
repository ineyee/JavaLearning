package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/product")
@Tag(name = "产品模块")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/get")
    @Operation(summary = "获取产品详情")
    public HttpResult<ProductDetailDto> get(@Valid ProductGetQuery query) throws ServiceException {
        ProductDetailDto data = productService.get(query);
        return HttpResult.ok(data);
    }
}
