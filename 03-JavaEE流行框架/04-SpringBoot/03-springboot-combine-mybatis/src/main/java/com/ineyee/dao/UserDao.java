package com.ineyee.dao;

import com.ineyee.domain.User;

import java.util.List;
import java.util.Map;

// 一张表对应的 dao，我们一般会把它定义成接口，然后为这个接口编写多个实现类
// 因为实际开发中我们很可能需要根据实际情况切换访问数据库的方案，比如 JDBC、Hibernate、MyBatis 等，这种面向接口编程的方式方便切换方案
public interface UserDao {
    /**
     * 获取一个用户
     *
     * @param id 用户 id
     * @return 用户 domain
     * ① null 代表数据库里没有这条数据
     * ② domain 代表数据库里有这条数据
     */
    User get(Integer id);

    /**
     * 分页获取用户列表
     *
     * @param params 分页参数需包含 limit（一页几条数据）和 offset（从哪个位置开始获取数据）两个字段
     *               因为 user.xml 文件里是直接的 SQL 语句只能接收 limit 和 offset，不能接收 pageSize 和 pageNum
     * @return 用户 domain 列表
     * ① [domain] 代表本次查询有数据
     * ② [] 代表本次查询没有数据
     */
    List<User> list(Map<String, Object> params);

    /**
     * 分页获取用户列表
     * 参数由 PageHelper 插件管理，所以这个方法就不用定义参数了
     *
     * @return 用户 domain 列表
     * ① [domain] 代表本次查询有数据
     * ② [] 代表本次查询没有数据
     */
    List<User> listPageHelper();

    /**
     * 新增一个用户
     *
     * @param User 用户 domain
     * @return 影响的数据条数
     * ① 影响的数据条数 > 0 代表保存成功，此时 MyBatis 会自动回填传进来 User 的其它参数，我们一般是给客户端响应刚保存成功的那条完整数据 data=domain，这样客户端就不用再查询一遍了
     * ② 影响的数据条数 <= 0 代表保存失败，我们一般是给客户端响应 data=null
     */
    int save(User User);

    /**
     * 批量新增用户
     * 这种批量操作是通过单条 INSERT INTO 拼接多 VALUES 实现的，它的特点是批量操作中的任何一个操作失败，那整个操作就整体失败，不会出现部分成功部分失败的情况
     * SQL example: INSERT INTO t_user (name, age, height, email) VALUES ('张三',  18, 1.88, 'zhangsan@qq.com'), ('李四', 19, 1.99, 'lisi@qq.com'), ('王五', 20, 2.00, 'wangwu@qq.com');
     *
     * @param UserList 用户 domain 列表
     * @return 影响的数据条数
     * ① 影响的数据条数 > 0 代表保存成功，此时 MyBatis 会自动回填传进来 UserList 的 id 参数，我们一般是给客户端响应刚保存成功那批数据的 id data = [domain.id]，客户端可以根据 id 数组再手动查询一次数据库拿到完整数据
     * ② 影响的数据条数 <= 0 代表保存失败，我们一般是给客户端响应 data=null
     */
    int saveBatch(List<User> UserList);

    /**
     * 删除一个用户
     *
     * @param id 用户 id
     * @return 影响的数据条数
     * ① 影响的数据条数 > 0 代表删除成功，我们一般是给客户端返回成功信息、因为客户端本来就知道删除的是哪条数据
     * ② 影响的数据条数 <= 0 代表删除失败，我们一般是给客户端返回失败信息
     */
    int remove(Integer id);

    /**
     * 批量删除用户
     * 这种批量操作是通过 WHERE IN + executeUpdate 实现的，它的特点是批量操作中的任何一个操作失败，那整个操作就整体失败，不会出现部分成功部分失败的情况
     * SQL example: DELETE FROM t_user WHERE id IN (1, 2, 3);
     *
     * @param idList 用户 id 列表
     * @return 影响的数据条数
     * ① 影响的数据条数 > 0 代表删除成功，我们一般是给客户端返回成功信息、因为客户端本来就知道删除的是哪批数据
     * ② 影响的数据条数 <= 0 代表删除失败，我们一般是给客户端返回失败信息
     */
    int removeBatch(List<Integer> idList);

    /**
     * 更新一个用户
     *
     * @param params 更新参数需包含 id 字段（用户唯一标识）和要更新的若干其它字段
     *               因为 user.xml 文件里是直接的 SQL 语句只能接收 Map 形式，不能分开接收 id 和 fieldsToUpdate
     * @return 影响的数据条数
     * ① 影响的数据条数 > 0 代表更新成功，我们一般是给客户端返回成功信息、因为客户端本来就知道更新的是哪条数据、哪些字段
     * ② 影响的数据条数 <= 0 代表更新失败，我们一般是给客户端返回失败信息
     */
    int update(Map<String, Object> params);

    /**
     * 批量更新用户
     * 这种批量操作是通过 WHERE IN + executeUpdate 实现的，它的特点是批量操作中的任何一个操作失败，那整个操作就整体失败，不会出现部分成功部分失败的情况
     * SQL example: UPDATE t_user SET age = 31, height = 1.75 WHERE id IN (1, 2, 3);
     *
     * @param params 更新参数需包含 idList 字段（用户唯一标识数组）和要更新的若干其它字段
     *               因为 user.xml 文件里是直接的 SQL 语句只能接收 Map 形式，不能分开接收 idList 和 fieldsToUpdate
     * @return 影响的数据条数
     * ① 影响的数据条数 > 0 代表更新成功，我们一般是给客户端返回成功信息、因为客户端本来就知道更新的是哪批数据、哪些字段
     * ② 影响的数据条数 <= 0 代表更新失败，我们一般是给客户端返回失败信息
     */
    int updateBatch(Map<String, Object> params);
}

