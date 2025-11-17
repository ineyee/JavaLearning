package com.ineyee._03_notnewdomainiocdi;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

// FactoryBean 类，类似于前面的工厂类
// 必须实现 Spring 自带的 FactoryBean 接口，并且指定我们要创建的 Bean 类型
// 这个类是用来创建 connection 对象的，而不是用来创建它自己的对象的，所以 beanId 直接叫 connection 即可，不能要默认的 connectionFactoryBean
@Component("connection")
@PropertySource("database.properties")
public class ConnectionFactoryBean implements FactoryBean<Connection> {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    @Value("${driverClassName}")
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Value("${url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${username}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${password}")
    public void setPassword(String password) {
        this.password = password;
    }

    // 这个方法类似于前面的静态方法或实例方法
    @Override
    public Connection getObject() throws Exception {
        // 第一步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
        Class.forName(driverClassName);
        // 第二步：利用 DriverManager 创建一个数据库连接对象 connection
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

    // 这个方法固定返回我们要创建的 Bean 类型
    @Override
    public Class<?> getObjectType() {
        return Connection.class;
    }
}