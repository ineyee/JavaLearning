package com.ineyee._01_staticfactory;

import java.sql.Connection;
import java.sql.DriverManager;

// 工厂类
public class ConnectionFactory {
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mysqlroot";

    // 静态方法
    public static Connection getConnection() throws Exception {
        // 第一步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
        Class.forName(DRIVER_CLASS_NAME);
        // 第二步：利用 DriverManager 创建一个数据库连接对象 connection
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        return connection;
    }
}