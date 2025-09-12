/*
  事务（Transaction）的核心理念是确保多个操作要么全部成功，要么全部失败，从而维护数据的完整性和一致性
  直白地说，事务就是指把多条 SQL 语句当作一个操作序列来执行，只有所有的 SQL 语句都执行成功这个操作序列才算执行成功，其中任意一条 SQL 语句执行失败这个操作序列就算执行失败，失败后我们可以把数据回滚到原始状态。事务一般是以开启事务开始，提交事务或回滚事务结束

  比如张三给李四转钱，张三扣钱这个操作和李四加钱这个操作就应该当做一个操作序列来执行，只有张三扣钱成功 + 李四加钱成功同时发生这个操作才能算成功，如果不采用事务来做，那很可能出现张三扣钱成功了，李四加钱的时候刚好数据库崩溃了，这样结果就是张三白白丢了钱

  所以只有需要把多条 SQL 语句当做当作一个操作序列来执行时才需要用到事务，比如转账（扣钱 + 加钱）、下单（扣库存 + 生成订单 + 写日志）等，平常单条 SQL 语句的增删改查操作一般不需要用到事务
*/

package com.ineyee.hello_mysql._03_jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class _0305_事务的使用 {
    private static DataSource connectionPool;

    static {
        try (InputStream is = _0304_连接池的使用.class.getClassLoader().getResourceAsStream("druid.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            connectionPool = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        transferMoney("张三", "李四", 200);
    }

    // 转账方法
    private static void transferMoney(String sender, String receiver, double amount) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        try {
            // 1、从连接池里获取一个连接，确保事务里的所有操作都在同一个连接上执行，事务里的操作如果在不同的连接上执行，那数据就会出错，就失去了事务的意义
            // 使用事务的场景，一定要通过获取一个连接的方式来做，但是一旦我们手动获取了连接，就必须手动释放连接，否则连接就会泄漏，导致连接池耗尽
            conn = connectionPool.getConnection();

            // 2、关闭自动提交，开启事务
            // JDBC 默认每条 SQL 都是自动提交的。这里关闭它，才能把多条 SQL 作为一个事务来执行。
            conn.setAutoCommit(false);

            // 3、扣款
            String sql1 = "UPDATE accounts SET balance = balance - ? WHERE user = ?";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setDouble(1, amount);
            pstmt1.setString(2, sender);
            pstmt1.executeUpdate();

            // 4、收款
            String sql2 = "UPDATE accounts SET balance = balance + ? WHERE user = ?";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setDouble(1, amount);
            pstmt2.setString(2, receiver);
            pstmt2.executeUpdate();

            // 5、提交事务
            conn.commit();
            System.out.println("转账成功！");
        } catch (Exception e) {
            System.err.println("转账失败: " + e.getMessage());
            // 回滚事务
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // 6、释放资源（将连接归还给连接池）
            // 注意：不是关闭物理连接，是归还给连接池
            try {
                if (pstmt1 != null) pstmt1.close();
                if (pstmt2 != null) pstmt2.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}