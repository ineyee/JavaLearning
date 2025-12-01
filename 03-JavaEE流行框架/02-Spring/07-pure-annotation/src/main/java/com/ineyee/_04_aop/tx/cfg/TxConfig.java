package com.ineyee._04_aop.tx.cfg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

// 事务管理器
@Configuration
// 这个注解是“事务管理的注解驱动”的功能
@EnableTransactionManagement
public class TxConfig {
    // 自定义属性，间接注入值
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 通过 @Bean 注解的方法来创建对象
    @Bean
    public DataSourceTransactionManager txMgr() {
        DataSourceTransactionManager txMgr = new DataSourceTransactionManager();
        // 通过 setter 方法把注入的值手动赋值
        txMgr.setDataSource(dataSource);
        return txMgr;
    }
}
