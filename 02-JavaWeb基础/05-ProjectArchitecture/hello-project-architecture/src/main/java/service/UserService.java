package service;

import bean.UserBean;
import exception.ServiceException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// 一张表对应的 service，我们一般会把它定义成接口，然后为这个接口编写多个实现类
// 因为实际开发中我们很可能需要根据实际情况切换访问数据层的方案，这种面向接口编程的方式方便切换方案
public interface UserService {
    /**
     * 新增用户，支持批量
     *
     * @param userBeanList 用户 bean 列表
     * @return 是否成功
     * @throws SQLException     业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean save(List<UserBean> userBeanList) throws SQLException, ServiceException;

    /**
     * 删除用户，支持批量
     *
     * @param idList 用户 id 列表
     * @return 是否成功
     * @throws SQLException     业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean remove(List<Integer> idList) throws SQLException, ServiceException;

    /**
     * 更新用户，支持批量
     *
     * @param idList         用户 id 列表
     * @param fieldsToUpdate 要更新的字段
     * @return 是否成功
     * @throws SQLException     业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    Boolean update(List<Integer> idList, Map<String, Object> fieldsToUpdate) throws SQLException, ServiceException;

    /**
     * 获取一个用户
     *
     * @param id 用户 id
     * @return 用户 bean
     * ① null 代表数据库里没有这条数据
     * ② bean 代表数据库里有这条数据
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    UserBean get(Integer id) throws SQLException;

    /**
     * 分页获取用户列表
     *
     * @param pageSize    一页几条数据
     * @param currentPage 当前第几页
     * @return 用户 bean 列表
     * ① [] 代表本次查询没有数据
     * ② [bean] 代表本次查询有数据
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<UserBean> list(Integer pageSize, Integer currentPage) throws SQLException;
}
