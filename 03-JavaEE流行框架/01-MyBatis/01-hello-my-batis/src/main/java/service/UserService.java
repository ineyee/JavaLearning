package service;

import bean.UserBean;
import exception.ServiceException;

import java.util.List;
import java.util.Map;

// 一张表对应的 service，我们一般会把它定义成接口，然后为这个接口编写多个实现类
// 因为实际开发中我们很可能需要根据实际情况切换访问数据层的方案，这种面向接口编程的方式方便切换方案
public interface UserService {
    /**
     * 获取一个用户
     *
     * @param id 用户 id
     * @return 用户 bean
     * ① null 代表数据库里没有这条数据
     * ② bean 代表数据库里有这条数据
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    UserBean get(Integer id) throws ServiceException;

    /**
     * 分页获取用户列表
     *
     * @param pageSize 一页几条数据
     * @param pageNum  当前第几页
     * @return 用户 bean 列表
     * ① [bean] 代表本次查询有数据
     * ② [] 代表本次查询没有数据
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<UserBean> list(Integer pageSize, Integer pageNum) throws ServiceException;

    /**
     * 分页获取用户列表
     *
     * @param pageSize 一页几条数据
     * @param pageNum  当前第几页
     * @return 用户 bean 列表
     * ① [bean] 代表本次查询有数据
     * ② [] 代表本次查询没有数据
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<UserBean> listPageHelper(Integer pageSize, Integer pageNum) throws ServiceException;

    /**
     * 新增一个用户
     *
     * @param userBean 用户 bean
     * @return 保存结果
     * ① bean 代表保存成功的那条完整数据，这样客户端就不用再查询一遍了
     * ② null 代表保存失败
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    UserBean save(UserBean userBean) throws ServiceException;

    /**
     * 批量新增用户
     *
     * @param userBeanList 用户 bean 列表
     * @return 保存结果
     * ① [id] 代表保存成功那批数据的 id，客户端可以根据 id 数组再手动查询一次数据库拿到完整数据
     * ② [] 代表保存失败
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    List<Integer> saveBatch(List<UserBean> userBeanList) throws ServiceException;

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
