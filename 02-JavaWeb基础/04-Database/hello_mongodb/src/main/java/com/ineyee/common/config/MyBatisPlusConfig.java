package com.ineyee.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// MyBatisPlus 添加分页插件拦截器
@Configuration
// mapper 层是通过 @MapperScan 注解来扫描的，Spring 会自动创建所有的 mapper 对象并放入 IoC 容器中
@MapperScan("com.ineyee.mapper")
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件拦截器，指定数据库类型为 MySQL（如果配置多个插件, 切记分页最后添加）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
