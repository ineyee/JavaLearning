package dao;

import bean.UserBean;
import util.DatabaseUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 用户模块接口的数据层
//
// 数据层（dao）的职责就是直接与数据库打交道，即调用数据层的“增删改” API + 传给这些 API 一个和 bean 直接相关的参数、你就可以获取到影响的数据条数，调用数据层的“查” API、你就可以获取到原始模型 bean
//
// 换句话说：
//  * 数据层只需要对自己负责就行，负责的体现就是干好创建 SQL 语句、执行 SQL 语句、获取执行结果、返回执行结果给业务层即可
//  * 其它的数据层就不用关心了，至于执行结果失败了怎样、成功了怎样，那是业务层拿到结果后该干的事
//
// 实践经验：
//  * 总是把执行成功的结果返回（虽然增删改操作执行成功的结果意义不大、查操作执行成功的结果意义才大），业务层用不用执行成功的结果由它自己决定
//  * 这里不需要 try-catch 执行失败的异常，往上层抛、抛到业务层
public class UserDao {
    /**
     * 新增一个用户
     *
     * @param userBean 用户 bean
     * @return 影响的数据条数
     * @throws SQLException           数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int saveUser(UserBean userBean) throws SQLException, ClassNotFoundException {
        String sql = """
                INSERT INTO t_user (
                    name,
                    age,
                    height,
                    email
                ) VALUES (
                    ?,
                    ?,
                    ?,
                    ?
                );
                """;
        return DatabaseUtil.executeUpdate(sql, userBean.getName(), userBean.getAge(), userBean.getHeight(), userBean.getEmail());
    }

    /**
     * 批量新增用户
     *
     * @param userBeanList 用户 bean 列表
     * @return 批量操作每一次操作影响数据的条数所构成的数组
     * @throws SQLException           数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int[] saveUserList(List<UserBean> userBeanList) throws SQLException, ClassNotFoundException {
        String sql = """
                INSERT INTO t_user (
                    name,
                    age,
                    height,
                    email
                ) VALUES (
                    ?,
                    ?,
                    ?,
                    ?
                );
                """;

        // 计算 SQL 语句中参数占位符 ? 的数量
        int argCount = DatabaseUtil.countArgsInSQL(sql);
        // 将 userBeanList 转换为 Object[][] 格式，一个二维数组
        // 第一维：代表有几个 userBean，其实就是一共需要执行几次插入操作
        // 第二维：代表每条插入操作对应 SQL 预处理语句里的参数数量，其实每次插入操作的参数数量都是固定的
        Object[][] batchArgs = new Object[userBeanList.size()][argCount];
        for (int i = 0; i < userBeanList.size(); i++) {
            UserBean userBean = userBeanList.get(i);
            batchArgs[i] = new Object[]{
                    userBean.getName(),
                    userBean.getAge(),
                    userBean.getHeight(),
                    userBean.getEmail()
            };
        }

        return DatabaseUtil.executeBatch(sql, batchArgs);
    }

    /**
     * 删除一个用户
     *
     * @param id 用户 id
     * @return 影响的数据条数
     * @throws SQLException           数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int removeUserById(Integer id) throws SQLException, ClassNotFoundException {
        String sql = """
                DELETE FROM t_user
                WHERE id = ?;
                """;
        return DatabaseUtil.executeUpdate(sql, id);
    }

    /**
     * 批量删除用户
     *
     * @param idList 用户 id 列表
     * @return 批量操作每一次操作影响数据的条数所构成的数组
     * @throws SQLException           数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int[] removeUserListById(List<Integer> idList) throws SQLException, ClassNotFoundException {
        String sql = """
                DELETE FROM t_user
                WHERE id = ?;
                """;

        // 计算 SQL 语句中参数占位符 ? 的数量
        int argCount = DatabaseUtil.countArgsInSQL(sql);
        // 将 idList 转换为 Object[][] 格式，一个二维数组
        // 第一维：代表有几个 id，其实就是一共需要执行几次删除操作
        // 第二维：代表每条插入操作对应 SQL 预处理语句里的参数数量，其实每次删除操作的参数数量都是固定的
        Object[][] batchArgs = new Object[idList.size()][argCount];
        for (int i = 0; i < idList.size(); i++) {
            batchArgs[i] = new Object[]{idList.get(i)};
        }

        return DatabaseUtil.executeBatch(sql, batchArgs);
    }

    /**
     * 更新一个用户
     *
     * @param id             用户 id
     * @param fieldsToUpdate 需要更新的字段及其值
     * @return 影响的数据条数
     * @throws SQLException           数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int updateUserById(Integer id, Map<String, Object> fieldsToUpdate) throws SQLException, ClassNotFoundException {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE t_user SET ");
        List<Object> params = new ArrayList<>();

        // 构建 SET 子句
        for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            sqlBuilder.append(fieldName).append(" = ?, ");
            params.add(fieldValue);
        }

        // 移除最后的逗号和空格
        if (sqlBuilder.toString().endsWith(", ")) {
            sqlBuilder.setLength(sqlBuilder.length() - 2);
        }

        sqlBuilder.append(" WHERE id = ?");
        params.add(id);

        return DatabaseUtil.executeUpdate(sqlBuilder.toString(), params.toArray());
    }

    /**
     * 批量更新用户
     *
     * @param idList         用户 id 列表
     * @param fieldsToUpdate 需要更新的字段及其值
     * @return 批量操作每一次操作影响数据的条数所构成的数组
     * @throws SQLException           数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public int[] updateUserListById(List<Integer> idList, Map<String, Object> fieldsToUpdate) throws SQLException, ClassNotFoundException {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE t_user SET ");
        List<Object> params = new ArrayList<>();

        // 构建 SET 子句
        for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            sqlBuilder.append(fieldName).append(" = ?, ");
            params.add(fieldValue);
        }

        // 移除最后的逗号和空格
        if (sqlBuilder.toString().endsWith(", ")) {
            sqlBuilder.setLength(sqlBuilder.length() - 2);
        }

        sqlBuilder.append(" WHERE id = ?");

        // 构建批量参数
        Object[][] batchArgs = new Object[idList.size()][params.size() + 1];
        for (int i = 0; i < idList.size(); i++) {
            // 复制字段更新参数
            for (int j = 0; j < params.size(); j++) {
                batchArgs[i][j] = params.get(j);
            }
            // 添加当前用户的 id
            batchArgs[i][params.size()] = idList.get(i);
        }

        return DatabaseUtil.executeBatch(sqlBuilder.toString(), batchArgs);
    }

    /**
     * 获取一个用户
     *
     * @param id 用户 id
     * @return 用户 bean
     * @throws SQLException           数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public UserBean getUserById(Integer id) throws SQLException, ClassNotFoundException {
        String sql = """
                SELECT *
                FROM t_user
                WHERE id = ?;
                """;
        return DatabaseUtil.executeQuery(sql, resultSet -> {
            UserBean userBean = new UserBean();
            userBean.setId(resultSet.getInt("id"));
            userBean.setName(resultSet.getString("name"));
            userBean.setAge(resultSet.getInt("age"));
            userBean.setHeight(resultSet.getDouble("height"));
            userBean.setEmail(resultSet.getString("email"));
            userBean.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
            userBean.setUpdateTime(resultSet.getTimestamp("update_time").toLocalDateTime());
            return userBean;
        }, id).get(0);
    }

    /**
     * 分页获取用户列表
     *
     * @param pageSize    一页几条数据
     * @param currentPage 当前第几页
     * @return 用户 bean 列表
     * @throws SQLException           数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 数据层不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public List<UserBean> listUserByPage(int pageSize, int currentPage) throws SQLException, ClassNotFoundException {
        String sql = """
                SELECT *
                FROM t_user
                ORDER BY create_time DESC
                LIMIT ? OFFSET ?;
                """;
        return DatabaseUtil.executeQuery(sql, resultSet -> {
            UserBean userBean = new UserBean();
            userBean.setId(resultSet.getInt("id"));
            userBean.setName(resultSet.getString("name"));
            userBean.setAge(resultSet.getInt("age"));
            userBean.setHeight(resultSet.getDouble("height"));
            userBean.setEmail(resultSet.getString("email"));
            userBean.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
            userBean.setUpdateTime(resultSet.getTimestamp("update_time").toLocalDateTime());
            return userBean;
        }, pageSize, (currentPage - 1) * pageSize);
    }
}
