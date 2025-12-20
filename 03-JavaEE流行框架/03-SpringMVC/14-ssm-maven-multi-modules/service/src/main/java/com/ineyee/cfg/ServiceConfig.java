package com.ineyee.cfg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

// 业务层配置类
@Configuration
// 通过 @ComponentScan 注解告诉 Spring 框架哪个包里的类是通过注解实现 IoC 的
// Spring 框架就会扫描这个包里所有有注解的类来自动创建对象并放到 IoC 父容器里
//
// 父容器只需要扫描 service 所在的包即可
@ComponentScan("com.ineyee.service")
// 导入配置文件
@PropertySource("classpath:dao.properties")
// 导入数据层 dao 的配置类
@Import(DaoConfig.class)
// 2.2 这个注解是“事务管理的注解驱动”的功能，附加代码和切面就不用配置了
@EnableTransactionManagement
public class ServiceConfig {
    @Value("${mybatis.dataSource}")
    private String dataSource;
    // 为了配合 dataSource 动态获取数据源 Bean 对象
    @Autowired
    private ApplicationContext applicationContext;

    // 2.1 Spring 的事务管理器，用来进行事务管理
    @Bean
    public DataSourceTransactionManager txMgr() {
        DataSourceTransactionManager txMgr = new DataSourceTransactionManager();
        DataSource ds = applicationContext.getBean(dataSource, DataSource.class);
        txMgr.setDataSource(ds);
        return txMgr;
    }

    // 2.3
    // 然后我们想给哪个业务增加事务管理的代码
    // 就直接在这个业务类里加上一个 @Transactional 注解就完事了，这样一来这个业务类里所有的方法都会自动加上事务管理的代码
    // 当然我们也可以只在某一个方法上加上一个 @Transactional 注解
}
