package com.ineyee.service;

import com.ineyee.common.api.error.ProductServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.req.ProductCreateReq;
import com.ineyee.pojo.req.ProductDeleteReq;
import com.ineyee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository; // 用来执行【增删改 + 基本查】操作

    @Override
    public ProductDetailDto get(ProductGetQuery query) throws ServiceException {
        // 这里调用 MongoDB 在 repository 层提供的 findById 方法
        Optional<Product> optionalProduct = productRepository.findById(query.getId());
        // 查询出来时，手动判断下是否软删除
        if (optionalProduct.isEmpty() || optionalProduct.get().getDeleted() == 1) {
            // 如果数据库执行 MQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表数据库里肯定没有这条数据，抛个业务异常
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }

        return ProductDetailDto.from(optionalProduct.get());
    }

    @Override
    public String save(ProductCreateReq req) throws ServiceException {
        Product product = new Product();
        product.setName(req.getName());
        product.setDesc(req.getDesc());
        product.setPrice(req.getPrice());
        product.setDesignerList(req.getDesignerList());
        // 新增数据时，手动设置下软删除字段的初始值
        product.setDeleted(0);

        // 这里调用 MongoDB 在 repository 层提供的 insert 方法
        Product savedProduct = productRepository.insert(product);
        return savedProduct.getId();
    }

    // 这里是软删除，所以本质上是一个手动更新 deleted 字段的操作
    @Override
    public void remove(ProductDeleteReq req) throws ServiceException {
        // 先看看对应的数据存不存在
        Optional<Product> optionalProduct = productRepository.findById(req.getId());
        if (optionalProduct.isEmpty() || optionalProduct.get().getDeleted() == 1) {
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }

        // 存在的话，更新下 deleted 字段
        Product product = optionalProduct.get();
        product.setDeleted(1);
        // 重新把数据保存到数据库，只会更新 deleted 字段
        productRepository.save(product);
    }
}
