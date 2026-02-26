package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.req.ProductCreateReq;
import com.ineyee.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save")
    @Operation(summary = "保存产品")
    public HttpResult<String> save(@Valid @RequestBody ProductCreateReq req) throws ServiceException {
        String id = productService.save(req);
        return HttpResult.ok(id);
    }
}
