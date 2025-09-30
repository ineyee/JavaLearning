package dao;

import bean.UserBean;
import util.DatabaseUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 用户模块接口的数据层
//
// 数据层（dao）的职责就是直接与数据库打交道，即：
//   * 调用数据层的“增删改” API + 传给这些 API 一个和 bean 直接相关的参数，你就可以获取到影响的数据条数
//   * 调用数据层的“查” API + 传给这些 API 一个和 bean 直接相关的参数，你就可以获取到原始模型 bean
//
// 换句话说：
//   * 数据层只需要对自己负责就行，负责的体现就是干好创建 SQL 语句、执行 SQL 语句、获取执行结果、返回执行结果给业务层即可
//   * 对上，数据层要绝对相信业务层，相信的体现就是直接拿着参数去做访问数据库，而不再纠结于参数会不会出问题，要绝对相信经过业务层校验后的参数是肯定没问题的
//   * 其它的数据层就不用关心了，至于执行结果失败了怎样、成功了怎样，那是业务层拿到结果后该干的事
//
// 实践经验：
//   * 执行成功时，总是把执行成功的结果返回（虽然增删改操作执行成功的结果意义不大、查操作执行成功的结果意义才大），业务层用不用执行成功的结果由它自己决定
//   * 执行失败时，这里不需要 try-catch 执行失败的异常，往上层抛、抛到业务层
public class UserDao {
    /**
     * 新增用户，支持批量
     * 这种批量操作是通过单条 INSERT INTO 拼接多 VALUES 实现的，它的特点是批量操作中的任何一个操作失败，那整个操作就整体失败，不会出现部分成功部分失败的情况
     * SQL example: INSERT INTO t_user (name, age, height, email) VALUES ('张三',  18, 1.88, 'zhangsan@qq.com'), ('李四', 19, 1.99, 'lisi@qq.com'), ('王五', 20, 2.00, 'wangwu@qq.com');
     *
     * @param userBeanList 用户 bean 列表
     * @return 影响的数据条数
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int save(List<UserBean> userBeanList) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO t_user (name, age, height, email) VALUES ");

        List<Object> args = new ArrayList<>();
        for (UserBean user : userBeanList) {
            sqlBuilder.append("(?, ?, ?, ?),");
            args.add(user.getName());
            args.add(user.getAge());
            args.add(user.getHeight());
            args.add(user.getEmail());
        }
        // 删掉最后的逗号
        sqlBuilder.setLength(sqlBuilder.length() - 1);

        return DatabaseUtil.executeUpdate(sqlBuilder.toString(), args.toArray());
    }

    /**
     * 删除用户，支持批量
     * 这种批量操作是通过 WHERE IN + executeUpdate 实现的，它的特点是批量操作中的任何一个操作失败，那整个操作就整体失败，不会出现部分成功部分失败的情况
     * SQL example: DELETE FROM t_user WHERE id IN (1, 2, 3);
     *
     * @param idList 用户 id 列表
     * @return 影响的数据条数
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int remove(List<Integer> idList) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM t_user WHERE id IN (");
        sqlBuilder.append("?, ".repeat(idList.size()));
        // 删掉最后的逗号和空格
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        sqlBuilder.append(")");

        return DatabaseUtil.executeUpdate(sqlBuilder.toString(), idList.toArray());
    }

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
    public int update(List<Integer> idList, Map<String, Object> fieldsToUpdate) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> args = new ArrayList<>();
        sqlBuilder.append("UPDATE t_user SET ");

        // 构建 SET 子句部分及其参数
        for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            sqlBuilder.append(fieldName).append(" = ?, ");
            args.add(fieldValue);
        }
        // 删掉最后的逗号和空格
        sqlBuilder.setLength(sqlBuilder.length() - 2);

        // 构建 WHERE IN 子句部分及其参数
        sqlBuilder.append(" WHERE id IN (");
        sqlBuilder.append("?, ".repeat(idList.size()));
        args.addAll(idList);
        // 删掉最后的逗号和空格
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        sqlBuilder.append(")");

        return DatabaseUtil.executeUpdate(sqlBuilder.toString(), args.toArray());
    }

    /**
     * 获取一个用户
     *
     * @param id 用户 id
     * @return 用户 bean
     * ① null 代表数据库里没有这条数据
     * ② bean 代表数据库里有这条数据
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public UserBean get(Integer id) throws SQLException {
        String sql = queryBaseSql() + """
                WHERE id = ?;
                """;
        List<UserBean> userBeanList = DatabaseUtil.executeQuery(sql, resultSetBeanMapper(), id);
        return userBeanList.isEmpty() ? null : userBeanList.get(0);
    }

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
    public List<UserBean> list(Integer pageSize, Integer currentPage) throws SQLException {
        String sql = queryBaseSql() + """
                ORDER BY create_time DESC
                LIMIT ? OFFSET ?;
                """;
        return DatabaseUtil.executeQuery(sql, resultSetBeanMapper(), pageSize, (currentPage - 1) * pageSize);
    }

    // get 和 list 的公共代码
    private String queryBaseSql() {
        return """
                SELECT
                    id,
                    name,
                    age,
                    height,
                    email,
                    create_time,
                    update_time
                FROM t_user
                """;
    }

    // get 和 list 的公共代码
    private DatabaseUtil.ResultSetBeanMapper<UserBean> resultSetBeanMapper() {
        return resultSet -> {
            UserBean userBean = new UserBean();
            userBean.setId(resultSet.getInt("id"));
            userBean.setName(resultSet.getString("name"));
            userBean.setAge(resultSet.getInt("age"));
            userBean.setHeight(resultSet.getDouble("height"));
            userBean.setEmail(resultSet.getString("email"));
            userBean.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
            userBean.setUpdateTime(resultSet.getTimestamp("update_time").toLocalDateTime());
            return userBean;
        };
    }

    /**
     * 根据邮箱查询用户数量
     *
     * @param email 用户邮箱
     * @return 用户数量
     * @throws SQLException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int countUserByEmail(String email) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS userCount
                FROM t_user
                WHERE email = ?;
                """;
        List<Integer> resultList = DatabaseUtil.executeQuery(sql, resultSet -> resultSet.getInt("userCount"), email);
        return resultList.isEmpty() ? 0 : resultList.get(0);
    }
}
