package com.ineyee._05_combinemybatis.cfg;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@PropertySource("classpath:mybatis.properties")
@MapperScan("${ms.mapperScan}") // 现在写个注解就可以了
public class MyBatisConfig {
    // =========== 数据源，将会被 sqlSessionFactory 引用 ==============
    // 开发环境和生产环境的数据库连接池及连接及数据库
    //     开发阶段，我们可以把默认环境设置为开发环境，从而访问测试数据库
    //     生产阶段，我们可以把默认环境设置为生产环境，从而访问正式数据库

    // 自定义属性，间接注入值
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

    // 通过 @Bean 注解的方法来创建对象
    @Bean
    public DataSource devDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        // 通过 setter 方法把注入的值手动赋值
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


    // =========== 数据源，将会被 sqlSessionFactory 引用 ==============

    // 自定义属性，间接注入值
    @Value("${ssf.typeAliasesPackage}")
    private String typeAliasesPackage;
    @Value("${ssf.mapperLocations}")
    private String mapperLocations;

    // 通过 @Bean 注解的方法来创建对象
    // 这里看似是返回 SqlSessionFactoryBean 对象，但实际上返回的是 SqlSessionFactoryBean 对象.getObject() 方法创建的 sqlSessionFactory 对象
    // 所以方法名我们直接就叫 sqlSessionFactory 了，而不是 sqlSessionFactoryBean
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 通过 setter 方法把注入的值手动赋值

        // 1
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);

        // 2
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);

        // 3
        com.github.pagehelper.PageInterceptor pageInterceptor = new com.github.pagehelper.PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        pageInterceptor.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});

        // 4
        sqlSessionFactoryBean.setDataSource(devDataSource());

        // 5
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));

        return sqlSessionFactoryBean;
    }
}
