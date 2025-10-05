package registerlogin.dao;

import registerlogin.bean.UserBean;
import registerlogin.util.DatabaseUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// 用户模块接口的数据层（JDBC 版）
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
public class UserDaoImpl implements UserDao {
    public int save(UserBean userBean) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO t_user (name, age, height, email, password) VALUES ");

        List<Object> args = new ArrayList<>();
        sqlBuilder.append("(?, ?, ?, ?, ?),");
        args.add(userBean.getName());
        args.add(userBean.getAge());
        args.add(userBean.getHeight());
        args.add(userBean.getEmail());
        args.add(userBean.getPassword());
        // 删掉最后的逗号
        sqlBuilder.setLength(sqlBuilder.length() - 1);

        return DatabaseUtil.executeUpdate(sqlBuilder.toString(), args.toArray());
    }

    public UserBean get(String email) throws SQLException {
        String sql = """
                SELECT
                    id,
                    name,
                    age,
                    height,
                    email,
                    password,
                    create_time,
                    update_time
                FROM t_user
                WHERE email = ?;
                """;
        List<UserBean> userBeanList = DatabaseUtil.executeQuery(sql, resultSet -> {
            UserBean userBean = new UserBean();
            userBean.setId(resultSet.getInt("id"));
            userBean.setName(resultSet.getString("name"));
            userBean.setAge(resultSet.getInt("age"));
            userBean.setHeight(resultSet.getDouble("height"));
            userBean.setEmail(resultSet.getString("email"));
            userBean.setPassword(resultSet.getString("password"));
            userBean.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
            userBean.setUpdateTime(resultSet.getTimestamp("update_time").toLocalDateTime());
            return userBean;
        }, email);
        return userBeanList.isEmpty() ? null : userBeanList.get(0);
    }
}
