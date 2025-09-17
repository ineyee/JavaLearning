package com.ineyee.hellomysql._03_jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class _0307_DatabaseUtil {
    // 连接池
    private static DataSource connectionPool;

    static {
        try (InputStream is = _0307_DatabaseUtil.class.getClassLoader().getResourceAsStream("druid.properties")) {
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
     * @return 影响的数据条数
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
    public static <T> List<T> executeQuery(String sql, _0307_DatabaseUtil.ResultSetBeanMapper<T> mapper, Object... args) throws SQLException, ClassNotFoundException {
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
