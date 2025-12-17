package com.ineyee.service;

import com.ineyee.api.exception.ServiceException;
import com.ineyee.domain.User;

import java.util.List;
import java.util.Map;

// 一张表对应的 service，我们一般会把它定义成接口，然后为这个接口编写多个实现类
// 因为实际开发中我们很可能需要根据实际情况切换访问数据层的方案，这种面向接口编程的方式方便切换方案
public interface UserService {
    /**
     * 获取一个用户
     *
     * @param id 用户 id
     * @return 用户 domain
     * ① null 代表数据库里没有这条数据
     * ② domain 代表数据库里有这条数据
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    User get(Integer id) throws ServiceException;

    /**
     * 分页获取用户列表
     *
     * @param pageSize 一页几条数据
     * @param pageNum  当前第几页
     * @return 用户 domain 列表
     * ① [domain] 代表本次查询有数据
     * ② [] 代表本次查询没有数据
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<User> list(Integer pageSize, Integer pageNum) throws ServiceException;

    /**
     * 分页获取用户列表
     *
     * @param pageSize 一页几条数据
     * @param pageNum  当前第几页
     * @return 用户 domain 列表
     * ① [domain] 代表本次查询有数据
     * ② [] 代表本次查询没有数据
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<User> listPageHelper(Integer pageSize, Integer pageNum) throws ServiceException;

    /**
     * 新增一个用户
     *
     * @param User 用户 domain
     * @return 保存结果
     * ① domain 代表保存成功的那条完整数据，这样客户端就不用再查询一遍了
     * ② null 代表保存失败
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    User save(User User) throws ServiceException;

    /**
     * 批量新增用户
     *
     * @param UserList 用户 domain 列表
     * @return 保存结果
     * ① [id] 代表保存成功那批数据的 id，客户端可以根据 id 数组再手动查询一次数据库拿到完整数据
     * ② [] 代表保存失败
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<Integer> saveBatch(List<User> UserList) throws ServiceException;

    /**
     * 删除一个用户
     *
     * @param id 用户 id
     * @return 是否成功
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean remove(Integer id) throws ServiceException;

    /**
     * 批量删除用户
     *
     * @param idList 用户 id 列表
     * @return 是否成功
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean removeBatch(List<Integer> idList) throws ServiceException;

    /**
     * 更新一个用户
     *
     * @param id             用户 id
     * @param fieldsToUpdate 要更新的字段
     * @return 是否成功
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean update(Integer id, Map<String, Object> fieldsToUpdate) throws ServiceException;

    /**
     * 批量更新用户
     *
     * @param idList         用户 id 列表
     * @param fieldsToUpdate 要更新的字段
     * @return 是否成功
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean updateBatch(List<Integer> idList, Map<String, Object> fieldsToUpdate) throws ServiceException;
}
