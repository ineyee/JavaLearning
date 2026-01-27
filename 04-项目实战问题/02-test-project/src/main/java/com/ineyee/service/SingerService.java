package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ineyee.common.api.exception.ServiceException;
import com.ineyee.pojo.po.Singer;
import com.ineyee.pojo.query.SingerListQuery;
import com.ineyee.pojo.req.SingerCreateBatchReq;
import com.ineyee.pojo.req.SingerCreateReq;
import com.ineyee.pojo.req.SingerUpdateBatchReq;
import com.ineyee.pojo.req.SingerUpdateReq;
import com.ineyee.pojo.vo.ListData;

import java.util.List;

public interface SingerService extends IService<Singer> {
    /**
     * 新增一个用户
     *
     * @param req 请求参数模型
     * @return 保存结果
     * ① pojo 代表保存成功的那条完整数据，这样客户端就不用再查询一遍了
     * ② null 代表保存失败
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Singer save(SingerCreateReq req) throws ServiceException;

    /**
     * 批量新增用户
     *
     * @param req 请求参数模型
     * @return 保存结果
     * ① [id] 代表保存成功那批数据的 id，客户端可以根据 id 数组再手动查询一次数据库拿到完整数据
     * ② [] 代表保存失败
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<Long> saveBatch(SingerCreateBatchReq req) throws ServiceException;

    /**
     * 更新一个用户
     *
     * @param req 请求参数模型
     * @return 是否成功
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean update(SingerUpdateReq req) throws ServiceException;

    /**
     * 批量更新用户
     *
     * @param req 请求参数模型
     * @return 是否成功
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean updateBatch(SingerUpdateBatchReq req) throws ServiceException;

    /**
     * 分页获取用户列表
     *
     * @param query 请求参数模型
     * @return 列表数据专用响应模型
     * 之前的查询结果仅仅是 List<T>，所以我们就通过返回值直接返回了
     * 但是现在的查询结果需要有 List<T>、pageNum、pageSize、total、totalPages，所以我们就定义了一个 ListData<T> 来专门返回
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    ListData<Singer> list(SingerListQuery query) throws ServiceException;
}