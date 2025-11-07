package com.ineyee._02_instancefactory;

import java.sql.Connection;
import java.sql.DriverManager;

// 工厂类
public class ConnectionFactory {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 实例方法
    public Connection getConnection() throws Exception {
        // 第一步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
        Class.forName(driverClassName);
        // 第二步：利用 DriverManager 创建一个数据库连接对象 connection
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }
}