package com.ineyee.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.common.api.error.CommonServiceError;
import com.ineyee.common.api.error.ProductServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.mapper.ProductMapper;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.query.ProductListQuery;
import com.ineyee.pojo.req.*;
import com.ineyee.common.api.ListData;
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
        // 这里调用 MyBatisPlus 在 service 层提供的 get 方法，不再直接调用 mapper 层的 selectById 方法
        Product product = getById(query.getId());
        if (product == null) {
            // 如果数据库执行 SQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表数据库里肯定没有这条数据，抛个业务异常
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }
        return product;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ListData<Product> list(ProductListQuery query) {
        // wrapper 用来添加查询条件
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        // 自定义参数
        if (query.getMinPrice() != null) {
            wrapper.ge(Product::getPrice, query.getMinPrice());
        }
        if (query.getMaxPrice() != null) {
            wrapper.le(Product::getPrice, query.getMaxPrice());
        }

        // 排序：先按创建时间降序排序，如果创建时间相同、再按 id 升序排序
        wrapper.orderByDesc(Product::getCreateTime)
                .orderByAsc(Product::getId);

        // 有模糊搜索参数
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            // 在 name 和 description 字段中模糊搜索
            wrapper.and(w ->
                    w.like(Product::getName, query.getKeyword())
                            .or()
                            .like(Product::getDescription, query.getKeyword())
            );
        }

        if (query.getPageNum() != null && query.getPageSize() != null) { // 要搞分页，别忘了在 MyBatisPlusConfig 里添加一下 MyBatisPlus 的分页插件
            Page<Product> page = new Page<>(query.getPageNum(), query.getPageSize());
            // 这里调用 MyBatisPlus 在 service 层提供的 page 方法，不再直接调用 mapper 层的 selectPage 方法
            Page<Product> queryedPage = page(page, wrapper);

            // 组装列表查询结果
            return ListData.fromPage(queryedPage);
        } else { // 不搞分页
            // 这里调用 MyBatisPlus 在 service 层提供的 list 方法，不再直接调用 mapper 层的 selectList 方法
            List<Product> productList = list(wrapper);

            // 组装列表查询结果
            return ListData.fromList(productList);
        }
    }

    @Override
    public Product save(ProductCreateReq req) throws ServiceException {
        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());

        // 这里调用 MyBatisPlus 在 service 层提供的 save 方法，不再直接调用 mapper 层的 insert 方法
        boolean success = save(product);
        if (!success) {
            // 如果数据库执行 SQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表是有业务异常，但是也太好说具体什么业务异常，索性抛个通用的业务异常
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        return product;
    }

    @Override
    public List<Long> saveBatch(ProductCreateBatchReq req) throws ServiceException {
        List<Product> productList = new ArrayList<>();
        req.getProductList().forEach(item -> {
            Product product = new Product();
            product.setName(item.getName());
            product.setDescription(item.getDescription());
            product.setPrice(item.getPrice());
            productList.add(product);
        });

        // 这里调用 MyBatisPlus 在 service 层提供的 saveBatch 方法，不再直接调用 mapper 层的 insertBatch 方法
        boolean success = saveBatch(productList);
        if (!success) {
            // 如果数据库执行 SQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表是有业务异常，但是也太好说具体什么业务异常，索性抛个通用的业务异常
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
        List<Long> productIdList = new ArrayList<>();
        productList.forEach(item -> {
            productIdList.add(item.getId());
        });
        return productIdList;
    }

    @Override
    public void remove(ProductDeleteReq req) throws ServiceException {
        // 这里调用 MyBatisPlus 在 service 层提供的 remove 方法，不再直接调用 mapper 层的 deleteById 方法
        boolean success = removeById(req.getId());
        if (!success) {
            // 如果数据库执行 SQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表数据库里肯定没有这条数据，抛个业务异常
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }
    }

    @Override
    public void removeBatch(ProductDeleteBatchReq req) throws ServiceException {
        // 这里调用 MyBatisPlus 在 service 层提供的 removeBatchByIds 方法，不再直接调用 mapper 层的 deleteByIds 方法
        boolean success = removeBatchByIds(req.getIdList());
        if (!success) {
            // 如果数据库执行 SQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表数据库里肯定没有这条数据，抛个业务异常
            throw new ServiceException(ProductServiceError.PRODUCT_NOT_EXIST);
        }
    }

    @Override
    public void update(ProductUpdateReq req) throws ServiceException {
        Product product = new Product();
        product.setId(req.getId());
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());

        // 这里调用 MyBatisPlus 在 service 层提供的 updateById 方法，不再直接调用 mapper 层的 updateById 方法
        boolean success = updateById(product);
        if (!success) {
            // 如果数据库执行 SQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表是有业务异常，但是也太好说具体什么业务异常，索性抛个通用的业务异常
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }

    @Override
    public void updateBatch(ProductUpdateBatchReq req) throws ServiceException {
        List<Product> productList = new ArrayList<>();
        req.getProductList().forEach(item -> {
            Product product = new Product();
            product.setId(item.getId());
            product.setName(item.getName());
            product.setDescription(item.getDescription());
            product.setPrice(item.getPrice());
            productList.add(product);
        });

        // 这里调用 MyBatisPlus 在 service 层提供的 updateBatchById 方法，不再直接调用 mapper 层的 updateBatchById 方法
        boolean success = updateBatchById(productList);
        if (!success) {
            // 如果数据库执行 SQL 时发生了异常，就会抛出系统异常，根本执行不到这里
            // 能执行到这里就代表是有业务异常，但是也太好说具体什么业务异常，索性抛个通用的业务异常
            throw new ServiceException(CommonServiceError.REQUEST_ERROR);
        }
    }
}
