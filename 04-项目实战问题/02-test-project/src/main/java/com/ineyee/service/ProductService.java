package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.dto.ProductDetailDto;
import com.ineyee.pojo.dto.ProductListDto;
import com.ineyee.pojo.po.Product;
import com.ineyee.pojo.query.ProductGetQuery;
import com.ineyee.pojo.query.ProductListQuery;
import com.ineyee.pojo.req.*;
import com.ineyee.common.api.ListData;

import java.util.List;

public interface ProductService extends IService<Product> {
    /**
     * 单个查询
     *
     * @param query 请求参数模型
     * @return pojo
     * ① 数据库里有这条数据时，该方法才会正常返回
     * ② 数据库里没有这条数据时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    ProductDetailDto get(ProductGetQuery query) throws ServiceException;

    /**
     * 列表查询
     *
     * @param query 请求参数模型
     * @return 列表查询专用响应模型
     * 之前的查询结果仅仅是 List<T>，所以我们就通过返回值直接返回了
     * 但是现在的查询结果需要有 List<T>、pageNum、pageSize、total、totalPages，所以我们就定义了一个 ListData<T> 来专门返回
     * ① 列表查询专用响应模型.[pojo] 代表本次查询有数据
     * ② 列表查询专用响应模型.[] 代表本次查询没有数据，列表查询没数据时抛异常不太合理
     */
    ListData<ProductListDto> list(ProductListQuery query);

    /**
     * 单个保存
     *
     * @param req 请求参数模型
     * @return id
     * ① 保存成功时，该方法才会正常返回保存成功那条数据的 id，客户端可以根据 id 再手动查询一次数据库拿到完整数据
     * （之所以不在这里直接返回保存成功的那条数据，是因为很可能客户端需要的数据是多表联查后数据，而不仅仅是这一个表的数据
     * 比如 song 保存成功后，返回列表展示时，song 里面还得有歌手的信息，所以不如让客户端走下 get 接口，拿到想要的数据）
     * ② 保存失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Long save(ProductCreateReq req) throws ServiceException;

    /**
     * 批量保存
     *
     * @param req 请求参数模型
     * @return [id]
     * ① 保存成功时，该方法才会正常返回保存成功那批数据的 id，客户端可以根据 id 数组再手动查询一次数据库拿到完整数据
     * ② 保存失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<Long> saveBatch(ProductCreateBatchReq req) throws ServiceException;

    /**
     * 单个删除
     *
     * @param req 请求参数模型
     * @return void
     * ① 删除成功时，该方法才会正常结束
     * ② 删除失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    void remove(ProductDeleteReq req) throws ServiceException;

    /**
     * 批量删除
     *
     * @param req 请求参数模型
     * @return void
     * ① 删除成功时，该方法才会正常结束
     * ② 删除失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    void removeBatch(ProductDeleteBatchReq req) throws ServiceException;

    /**
     * 单个更新
     *
     * @param req 请求参数模型
     * @return void
     * ① 更新成功时，该方法才会正常结束
     * ② 更新失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    void update(ProductUpdateReq req) throws ServiceException;

    /**
     * 批量更新
     *
     * @param req 请求参数模型
     * @return void
     * ① 更新成功时，该方法才会正常结束
     * ② 更新失败时，该方法直接抛业务异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    void updateBatch(ProductUpdateBatchReq req) throws ServiceException;
}