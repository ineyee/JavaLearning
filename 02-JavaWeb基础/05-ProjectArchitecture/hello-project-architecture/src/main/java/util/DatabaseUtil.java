package util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// DatabaseUtil 是对数据库驱动和连接池的封装，调用 DatabaseUtil 你就可以获取到原始模型 bean（在 Java 的架构规范里一般不会直接把 rawData 往上抛）
// 但是这里返回的 bean 是泛型，不是具体的 bean，所以我们一般不把 DatabaseUtil 视作数据层，而仅仅把它视作一个数据库工具类
// 而把 Dao 才视作数据层，因为 Dao 里才真正履行了数据层的职责：创建 SQL 语句、执行 SQL 语句、获取执行结果、返回执行结果给业务层
public abstract class DatabaseUtil {
    // 连接池
    private static DataSource connectionPool;

    static {
        try (InputStream is = DatabaseUtil.class.getClassLoader().getResourceAsStream("druid.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            // 用配置文件创建连接池
            connectionPool = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行增、删、改操作
     *
     * @param sql  要执行的 SQL 预处理语句
     * @param args SQL 预处理语句里的参数
     * @return 影响的数据条数，0 代表没影响任何数据、因为数据库里没匹配到这条数据、但是没抛异常，>0 代表数据库里匹配到了这条数据且操作成功。总之 >0 才可以判定为执行成功
     * @throws SQLException           工具类不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 工具类不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public static int executeUpdate(String sql, Object... args) throws SQLException, ClassNotFoundException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                // 表的列号下标是从 1 开始的
                // 无论表里的字段是什么数据类型，这里统一用 setObject 设置
                preparedStatement.setObject(i + 1, args[i]);
            }

            return preparedStatement.executeUpdate();
        }
    }

    /**
     * 执行批量增、删、改操作
     * 注意：批量操作中途某一次操作失败时，会继续执行后续的操作，只是返回值里这次失败的操作对应影响数据的条数为一个负值代表出错
     *
     * @param sql       要执行的 SQL 预处理语句
     * @param batchArgs 这里代表可以传入任意个 Object[] 类型的参数 batchArg，而每一个 batchArg 则是批量操作其中某一次操作对应 SQL 预处理语句里的参数
     * @return 批量操作每一次操作影响的数据条数所构成的数组，0 代表没影响任何数据、因为数据库里没匹配到这条数据、但是没抛异常，>0 代表数据库里匹配到了这条数据且操作成功，<0 代表数据库里匹配到了这条数据且操作出错抛异常。总之 >0 才可以判定为执行成功
     * @throws SQLException           工具类不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 工具类不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public static int[] executeBatch(String sql, Object[]... batchArgs) throws SQLException, ClassNotFoundException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int[] results = new int[batchArgs.length];

            for (int rowIndex = 0; rowIndex < batchArgs.length; rowIndex++) {
                Object[] args = batchArgs[rowIndex];
                try {
                    for (int i = 0; i < args.length; i++) {
                        preparedStatement.setObject(i + 1, args[i]);
                    }
                    results[rowIndex] = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    results[rowIndex] = Statement.EXECUTE_FAILED;
                }
            }

            return results;
        }
    }

    /**
     * 计算 SQL 语句中参数占位符 ? 的数量
     *
     * @param sql SQL语句
     * @return 参数占位符 ? 的数量
     */
    public static int countArgsInSQL(String sql) {
        if (sql == null || sql.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (char c : sql.toCharArray()) {
            if (c == '?') {
                count++;
            }
        }
        return count;
    }

    /**
     * 执行查询操作
     *
     * @param sql    要执行的 SQL 预处理语句
     * @param mapper resultSet -> bean 回调函数
     * @param args   SQL 预处理语句里的参数
     * @param <T>    XxxBean
     * @return List<XxxBean>
     * @throws SQLException           工具类不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     * @throws ClassNotFoundException 工具类不负责处理异常，往上抛即可，最终会抛到 controller 层统一处理异常
     */
    public static <T> List<T> executeQuery(String sql, DatabaseUtil.ResultSetBeanMapper<T> mapper, Object... args) throws SQLException, ClassNotFoundException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                // 表的列号下标是从 1 开始的
                // 无论表里的字段是什么数据类型，这里统一用 setObject 设置
                preparedStatement.setObject(i + 1, args[i]);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                // resultSet -> bean
                list.add(mapper.map(resultSet));
            }
            return list;
        }
    }

    /**
     * executeQuery 方法会从数据库里查询出 resultSet
     * 但是 resultSet 怎么转换成 bean，我们是无法在这个工具类内部做决定的，因为外界各种 bean 的属性千差万别
     * 所以就得让外界自己去做转换，于是就用到了回调函数，让外界自己实现转换的代码并传进来即可
     * <p>
     * Java 里实现回调函数得用匿名类（抽象类的子类或接口的实现类） + 方法来实现
     * 而这里只需要一个方法就行，所以我们用函数式接口就行了，函数式接口的语法糖是 Lambda 表达式
     *
     * @param <T> 外界 bean 的真实类型
     */
    @FunctionalInterface
    public interface ResultSetBeanMapper<T> {
        /**
         * resultSet -> bean 的 方法
         *
         * @param resultSet 把查询到的每一条 resultSet 扔给外界，让外界自己拿着去转换成 bean
         * @return 转换后的 bean
         */
        T map(ResultSet resultSet) throws SQLException;
    }
}
