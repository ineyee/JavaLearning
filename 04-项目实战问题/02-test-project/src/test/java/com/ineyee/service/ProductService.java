package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

import java.util.List;

public interface ProductService extends IService<Product> {
    Product get(ProductGetQuery query) throws ServiceException;

    ListData<Product> list(ProductListQuery query);

    Product save(ProductCreateReq req) throws ServiceException;

    List<Long> saveBatch(ProductCreateBatchReq req) throws ServiceException;

    void remove(ProductDeleteReq req) throws ServiceException;

    void removeBatch(ProductDeleteBatchReq req) throws ServiceException;

    void update(ProductUpdateReq req) throws ServiceException;

    void updateBatch(ProductUpdateBatchReq req) throws ServiceException;
}
