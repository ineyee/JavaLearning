package com.ineyee.service.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.common.api.error.CommonServiceError;
import com.ineyee.common.api.error.ProductServiceError;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.mapper.ProductMapper;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.product.ProductGetQuery;
import com.ineyee.pojo.query.product.ProductListQuery;
import com.ineyee.pojo.req.product.*;
import com.ineyee.pojo.vo.ListData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 1、在 service 目录下创建一个 XxxServiceImpl 的空实现类即可
// 只要让实现类继承自 ServiceImpl，那么该实现类就自动拥有了众多接口方法的实现
// 泛型指定一下对应的 mapper 类 和 po 类
//
// 2、业务层
// 业务层（service）的职责就是处理我们实际的业务逻辑，如：
//     * 业务规则校验
//     * 请求参数的 pojo 转换，需要转换成业务方法能接收的参数
//     * 调用数据层的 API（现在改成了调用 MyBatisPlus 在 service 层提供的 CRUD 方法），等待数据层给它返回“影响的数据条数”或“原始模型”
//     * 响应体的 pojo 转换，需要把数据层返回的数据转换成表现层更方便使用的数据，比如“影响的数据条数”转换成“成功与否”、原始模型”转换成“完整 po 数据”等
// 换句话说：
//   * 对下，业务层要对数据层负责，负责的体现就是要做好业务规则校验，确保传递给数据层的参数是人家直接能用的，不用人家再做什么乱七八糟的校验
//   * 对上
//     * 业务层要绝对相信表现层，相信的体现就是直接拿着参数去做业务，而不再纠结于参数会不会出问题，要绝对相信经过表现层校验后的参数是肯定没问题的
//     * 业务层要对表现层负责，负责的体现就是要做好数据转换，把数据处理成表现层更方便使用的数据交付给表现层
//   * 其它的业务层就不用关心了，至于执行结果失败了怎样、成功了怎样，那是表现层拿到结果后该干的事
// 实践经验：
//  * 执行成功时，总是把执行成功的结果返回，表现层用不用执行成功的结果由它自己决定
//  * 执行失败时，这里不需要 try-catch 数据层 API 执行失败的异常，继续往上层抛、抛到表现层（由数据层抛上来的异常都是系统异常，最终会抛到全局异常处理那里进行统一处理）
//  * 业务逻辑出错时，我们需要在业务层主动抛出业务异常、抛到表现层（业务层主动抛出的异常都是业务异常，最终会抛到全局异常处理那里进行统一处理）
// 注意：
//  * 业务层绝对不是数据层的马甲层，它有存在的意义，比如同样都是 save 方法，数据层里的 save 就是直接往数据库里写数据
//  * 而业务层里的 save 则需要首先判断数据库里存不存在这个用户邮箱，存在的话就跳过，不存在的话再往数据库里写，因为用户邮箱是不允许重复的
//  * 也就是说，业务层的一个方法里可能会调用数据层里的多个方法来共同完成一个业务
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
