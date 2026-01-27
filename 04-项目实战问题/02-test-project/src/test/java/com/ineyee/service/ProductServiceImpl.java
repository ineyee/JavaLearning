package com.ineyee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.common.api.error.CommonServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.mapper.ProductMapper;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.query.ProductListQuery;
import com.ineyee.pojo.req.ProductCreateBatchReq;
import com.ineyee.pojo.req.ProductCreateReq;
import com.ineyee.pojo.req.ProductDeleteBatchReq;
import com.ineyee.pojo.req.ProductDeleteReq;
import com.ineyee.pojo.req.ProductUpdateBatchReq;
import com.ineyee.pojo.req.ProductUpdateReq;
import com.ineyee.pojo.vo.ListData;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Product get(ProductGetQuery query) throws ServiceException {
        Product data = getById(query.getId());
        if (data == null) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ListData<Product> list(ProductListQuery query) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        // TODO: 按需追加查询条件

        wrapper.orderByDesc(Product::getId);

        if (query.getPageNum() != null && query.getPageSize() != null) {
            Page<Product> page = new Page<>(query.getPageNum(), query.getPageSize());
            Page<Product> queriedPage = page(page, wrapper);
            return ListData.fromPage(queriedPage);
        } else {
            List<Product> dataList = list(wrapper);
            return ListData.fromList(dataList);
        }
    }

    @Override
    public Product save(ProductCreateReq req) throws ServiceException {
        Product entity = new Product();
        BeanUtils.copyProperties(req, entity);
        if (!save(entity)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        return entity;
    }

    @Override
    public List<Long> saveBatch(ProductCreateBatchReq req) throws ServiceException {
        List<Product> entityList = new ArrayList<>();
        req.getProductList().forEach(item -> {
            Product entity = new Product();
            BeanUtils.copyProperties(item, entity);
            entityList.add(entity);
        });
        if (!saveBatch(entityList)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        List<Long> idList = new ArrayList<>();
        entityList.forEach(item -> idList.add(item.getId()));
        return idList;
    }

    @Override
    public void remove(ProductDeleteReq req) throws ServiceException {
        if (!removeById(req.getId())) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void removeBatch(ProductDeleteBatchReq req) throws ServiceException {
        if (!removeBatchByIds(req.getIdList())) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void update(ProductUpdateReq req) throws ServiceException {
        Product entity = new Product();
        BeanUtils.copyProperties(req, entity);
        if (!updateById(entity)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void updateBatch(ProductUpdateBatchReq req) throws ServiceException {
        List<Product> entityList = new ArrayList<>();
        req.getProductList().forEach(item -> {
            Product entity = new Product();
            BeanUtils.copyProperties(item, entity);
            entityList.add(entity);
        });
        if (!updateBatchById(entityList)) {
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }
}
