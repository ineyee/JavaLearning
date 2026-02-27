package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.ListData;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.dto.ProductListDto;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.query.ProductListQuery;
import com.ineyee.pojo.req.*;
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

    @GetMapping("/list")
    @Operation(summary = "获取产品列表")
    public HttpResult<ListData<ProductListDto>> list(@Valid ProductListQuery query) {
        ListData<ProductListDto> dataList = productService.list(query);
        return HttpResult.ok(dataList);
    }

    @PostMapping("/save")
    @Operation(summary = "保存产品")
    public HttpResult<String> save(@Valid @RequestBody ProductCreateReq req) throws ServiceException {
        String id = productService.save(req);
        return HttpResult.ok(id);
    }

    @PostMapping("/remove")
    @Operation(summary = "删除产品")
    public HttpResult<Void> remove(@Valid @RequestBody ProductDeleteReq req) throws ServiceException {
        productService.remove(req);
        return HttpResult.ok();
    }

    @PostMapping("/update")
    @Operation(summary = "更新产品")
    public HttpResult<Void> update(@Valid @RequestBody ProductUpdateReq req) throws ServiceException {
        productService.update(req);
        return HttpResult.ok();
    }

    @PostMapping("/addDesigner")
    @Operation(summary = "添加设计师")
    public HttpResult<String> addDesigner(@Valid @RequestBody DesignerCreateReq req) throws ServiceException {
        String name = productService.addDesigner(req);
        return HttpResult.ok(name);
    }

    @PostMapping("/removeDesigner")
    @Operation(summary = "删除设计师")
    public HttpResult<Void> removeDesigner(@Valid @RequestBody DesignerRemoveReq req) throws ServiceException {
        productService.removeDesigner(req);
        return HttpResult.ok();
    }

    @PostMapping("/updateDesigner")
    @Operation(summary = "更新设计师")
    public HttpResult<Void> updateDesigner(@Valid @RequestBody DesignerUpdateReq req) throws ServiceException {
        productService.updateDesigner(req);
        return HttpResult.ok();
    }
}
