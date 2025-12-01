package com.ineyee._04_aop.tx.cfg;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

// 数据源，将会被事务管理器引用
@Configuration
// 开发环境和生产环境的数据库连接池及连接及数据库
//     开发阶段，我们可以把默认环境设置为开发环境，从而访问测试数据库
//     生产阶段，我们可以把默认环境设置为生产环境，从而访问正式数据库
@PropertySource("database-dev.properties")
public class DatabaseConfig {
    // 自定义属性，间接注入值
    @Value("${driverClassName}")
    private String driverClassName;
    @Value("${url}")
    private String url;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${initialSize}")
    private int initialSize;
    @Value("${maxActive}")
    private int maxActive;

    // 通过 @Bean 注解的方法来创建对象
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        // 通过 setter 方法把注入的值手动赋值
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        return dataSource;
    }
}
