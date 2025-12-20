package com.ineyee.cfg;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

// Spring 主配置类
// 1 开头的都是数据层 dao 的相关配置
// 2 开头的都是业务层 service 的相关配置
@Configuration
// 通过 @ComponentScan 注解告诉 Spring 框架哪个包里的类是通过注解实现 IoC 的
// Spring 框架就会扫描这个包里所有有注解的类来自动创建对象并放到 IoC 父容器里
//
// 父容器只需要扫描 service 所在的包即可
@ComponentScan("com.ineyee.service")
// 1.1 导入配置文件
@PropertySource("classpath:dao.properties")
// 1.4 配置一下 mapper 扫描器即可，这样一来 Spring 就会自动创建所有的 dao 对象并放入父 IoC 容器里了
@MapperScan("com.ineyee.dao")
// 2.2 这个注解是“事务管理的注解驱动”的功能，附加代码和切面就不用配置了
@EnableTransactionManagement
public class SpringConfig {
    /*
    1.2 开发环境和生产环境的数据库连接池、连接、数据库，数据源将会被 sqlSessionFactory 引用
        开发阶段，我们可以把默认环境设置为开发环境，从而访问测试数据库
        生产阶段，我们可以把默认环境设置为生产环境，从而访问正式数据库

        通过 @Bean 来实现 IoC，通过 @Bean 注解的方法手动设置法来实现 DI
     */
    @Value("${dev.driverClassName}")
    private String devDriverClassName;
    @Value("${dev.url}")
    private String devUrl;
    @Value("${dev.username}")
    private String devUsername;
    @Value("${dev.password}")
    private String devPassword;
    @Value("${dev.initialSize}")
    private int devInitialSize;
    @Value("${dev.maxActive}")
    private int devMaxActive;
    @Value("${prod.driverClassName}")
    private String prodDriverClassName;
    @Value("${prod.url}")
    private String prodUrl;
    @Value("${prod.username}")
    private String prodUsername;
    @Value("${prod.password}")
    private String prodPassword;
    @Value("${prod.initialSize}")
    private int prodInitialSize;
    @Value("${prod.maxActive}")
    private int prodMaxActive;

    @Bean
    public DataSource devDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(devDriverClassName);
        dataSource.setUrl(devUrl);
        dataSource.setUsername(devUsername);
        dataSource.setPassword(devPassword);
        dataSource.setInitialSize(devInitialSize);
        dataSource.setMaxActive(devMaxActive);
        return dataSource;
    }

    @Bean
    public DataSource prodDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(prodDriverClassName);
        dataSource.setUrl(prodUrl);
        dataSource.setUsername(prodUsername);
        dataSource.setPassword(prodPassword);
        dataSource.setInitialSize(prodInitialSize);
        dataSource.setMaxActive(prodMaxActive);
        return dataSource;
    }

    /*
    1.3 之前需要事先创建一个 MyBatisUtil 类来创建 SqlSessionFactory 对象
        现在直接通过 Spring 来创建 SqlSessionFactory 对象

        之前是在 MyBatisUtil 类里创建 SqlSessionFactory 对象的时候通过指定 MyBatis 的配置文件来让 SqlSessionFactory 对象知道 MyBatis 的各种配置
        现在直接通过 SqlSessionFactory 对象的属性即可设置

        通过 @Bean 来实现 IoC，通过 @Bean 注解的方法手动设置法来实现 DI
     */
    @Value("${mybatis.mapUnderscoreToCamelCase}")
    private Boolean mapUnderscoreToCamelCase;
    @Value("${mybatis.typeAliasesPackage}")
    private String typeAliasesPackage;
    @Value("${mybatis.dataSource}")
    private String dataSource;
    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;
    // 为了配合 dataSource 动态获取数据源 Bean 对象
    @Autowired
    private ApplicationContext applicationContext;


    // 这里看似是返回 SqlSessionFactoryBean 对象，但实际上返回的是 SqlSessionFactoryBean 对象.getObject() 方法创建的 sqlSessionFactory 对象
    // 所以方法名我们直接就叫 sqlSessionFactory 了，而不是 sqlSessionFactoryBean
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 是否开启驼峰命名自动映射，即数据库表自动转 Java Bean 时是否从经典数据库列名 create_time 映射到经典 Java 属性名 createTime
        // 默认 false
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        sqlSessionFactoryBean.setConfiguration(configuration);

        // 类型别名 typeAliases，用来给 xml 文件（如 mappers 里的 xml 文件、Spring 的配置文件等）里的类型取别名，如 type、parameterType、resultType 这种以 type 结尾的属性都是接收一个类型
        // 包名.类名，全类名，比较长；我们可以给全类名取个别名，短类名，比较短，写起来更方便；当然如果你偏好于写全类名，那也可以不定义别名
        //     方式一：name = 可以是整个包的方式，value = 默认就会把这个包下所有的全类名 com.ineyee.domain.Xxx 取别名为短类名 Xxx
        //     方式一：name = 也可以是单个类型的方式，value = 默认就会把这个全类名 com.ineyee.domain.Xxx 取别名为短类名 Xxx
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);

        // 插件：拦截器设置为 PageHelper 的 PageInterceptor
        // reasonable 设置为 true，代表使分页查询合理化：
        //     当 pageNum <= 0 时，自动返回第一页的数据
        //     当 pageNum > totalPage 时，自动返回最后一页的数据
        com.github.pagehelper.PageInterceptor pageInterceptor = new com.github.pagehelper.PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        pageInterceptor.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});

        // 动态获取数据源 Bean 对象
        // 开发阶段，我们可以把默认环境设置为开发环境，从而访问测试数据库
        // 生产阶段，我们可以把默认环境设置为生产环境，从而访问正式数据库
        DataSource ds = applicationContext.getBean(dataSource, DataSource.class);
        sqlSessionFactoryBean.setDataSource(ds);

        // 注册数据库表自动转 Java Bean 的映射文件
        // 这里我们不需要再一个一个映射文件注册了，直接通配符把所有的映射文件注册即可
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));

        return sqlSessionFactoryBean;
    }

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
