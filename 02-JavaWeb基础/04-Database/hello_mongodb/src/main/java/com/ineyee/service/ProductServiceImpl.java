package com.ineyee.service;

import com.ineyee.common.api.error.ProductServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.ProductGetQuery;
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
        Optional<Product> product = productRepository.findById(query.getId());
        if (product.isEmpty() || product.get().getDeleted() == 1) {
            // 如果数据库执行 MQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表数据库里肯定没有这条数据，抛个业务异常
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }

        return ProductDetailDto.from(product.get());
    }
}
