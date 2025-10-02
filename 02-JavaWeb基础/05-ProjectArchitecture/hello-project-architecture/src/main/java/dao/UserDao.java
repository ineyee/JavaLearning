package dao;

import bean.UserBean;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// 一张表对应的 dao，我们一般会把它定义成接口，然后为这个接口编写多个实现类
// 因为实际开发中我们很可能需要根据实际情况切换访问数据库的方案，比如 JDBC、Hibernate、MyBatis 等，这种面向接口编程的方式方便切换方案
public interface UserDao {
    /**
     * 新增用户，支持批量
     * 这种批量操作是通过单条 INSERT INTO 拼接多 VALUES 实现的，它的特点是批量操作中的任何一个操作失败，那整个操作就整体失败，不会出现部分成功部分失败的情况
     * SQL example: INSERT INTO t_user (name, age, height, email) VALUES ('张三',  18, 1.88, 'zhangsan@qq.com'), ('李四', 19, 1.99, 'lisi@qq.com'), ('王五', 20, 2.00, 'wangwu@qq.com');
     *
     * @param userBeanList 用户 bean 列表
     * @return 影响的数据条数
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    int save(List<UserBean> userBeanList) throws SQLException;

    /**
     * 删除用户，支持批量
     * 这种批量操作是通过 WHERE IN + executeUpdate 实现的，它的特点是批量操作中的任何一个操作失败，那整个操作就整体失败，不会出现部分成功部分失败的情况
     * SQL example: DELETE FROM t_user WHERE id IN (1, 2, 3);
     *
     * @param idList 用户 id 列表
     * @return 影响的数据条数
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    int remove(List<Integer> idList) throws SQLException;

    /**
     * 更新用户，支持批量
     * 这种批量操作是通过 WHERE IN + executeUpdate 实现的，它的特点是批量操作中的任何一个操作失败，那整个操作就整体失败，不会出现部分成功部分失败的情况
     * SQL example: UPDATE t_user SET age = 31, height = 1.75 WHERE id IN (1, 2, 3);
     *
     * @param idList         用户 id 列表
     * @param fieldsToUpdate 需要更新的字段及其值
     * @return 影响的数据条数
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    int update(List<Integer> idList, Map<String, Object> fieldsToUpdate) throws SQLException;

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

    /**
     * 根据邮箱查询用户数量
     *
     * @param email 用户邮箱
     * @return 用户数量
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    int countUserByEmail(String email) throws SQLException;
}
