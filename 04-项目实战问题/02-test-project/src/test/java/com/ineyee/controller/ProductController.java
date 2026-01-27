package com.ineyee.controller;

import com.ineyee.common.api.HttpResult;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.query.ProductListQuery;
import com.ineyee.pojo.req.ProductCreateReq;
import com.ineyee.pojo.req.ProductCreateBatchReq;
import com.ineyee.pojo.req.ProductDeleteReq;
import com.ineyee.pojo.req.ProductDeleteBatchReq;
import com.ineyee.pojo.req.ProductUpdateReq;
import com.ineyee.pojo.req.ProductUpdateBatchReq;
import com.ineyee.pojo.vo.ListData;
import com.ineyee.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/get")
    public HttpResult<Product> get(@Valid ProductGetQuery query) throws ServiceException {
        Product data = productService.get(query);
        return HttpResult.ok(data);
    }

    @GetMapping("/list")
    public HttpResult<ListData<Product>> list(@Valid ProductListQuery query) {
        ListData<Product> dataList = productService.list(query);
        return HttpResult.ok(dataList);
    }

    @PostMapping("/save")
    public HttpResult<Product> save(@Valid @RequestBody ProductCreateReq req) throws ServiceException {
        Product data = productService.save(req);
        return HttpResult.ok(data);
    }

    @PostMapping("/saveBatch")
    public HttpResult<List<Long>> saveBatch(@Valid @RequestBody ProductCreateBatchReq req) throws ServiceException {
        List<Long> idList = productService.saveBatch(req);
        return HttpResult.ok(idList);
    }

    @PostMapping("/remove")
    public HttpResult<Void> remove(@Valid @RequestBody ProductDeleteReq req) throws ServiceException {
        productService.remove(req);
        return HttpResult.ok();
    }

    @PostMapping("/removeBatch")
    public HttpResult<Void> removeBatch(@Valid @RequestBody ProductDeleteBatchReq req) throws ServiceException {
        productService.removeBatch(req);
        return HttpResult.ok();
    }

    @PostMapping("/update")
    public HttpResult<Void> update(@Valid @RequestBody ProductUpdateReq req) throws ServiceException {
        productService.update(req);
        return HttpResult.ok();
    }

    @PostMapping("/updateBatch")
    public HttpResult<Void> updateBatch(@Valid @RequestBody ProductUpdateBatchReq req) throws ServiceException {
        productService.updateBatch(req);
        return HttpResult.ok();
    }
}
