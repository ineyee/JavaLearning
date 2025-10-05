package registerlogin.service;

import registerlogin.bean.UserBean;
import registerlogin.exception.ServiceException;

import java.sql.SQLException;

// 一张表对应的 service，我们一般会把它定义成接口，然后为这个接口编写多个实现类
// 因为实际开发中我们很可能需要根据实际情况切换访问数据层的方案，这种面向接口编程的方式方便切换方案
public interface UserService {
    /**
     * 新增一个用户
     *
     * @param userBean 用户 bean
     * @return 是否成功
     * @throws SQLException     业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ServiceException 业务层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    UserBean save(UserBean userBean) throws SQLException, ServiceException;

    /**
     * 获取一个用户
     *
     * @param email 用户邮箱
     * @return 用户 bean
     * ① null 代表数据库里没有这条数据
     * ② bean 代表数据库里有这条数据
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    UserBean get(String email) throws SQLException;
}
