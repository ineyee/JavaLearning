package com.ineyee._02_DI.cfg;

import com.ineyee._02_DI.domain.ThirdPartyClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.ineyee._02_DI")
public class AppConfig {
    // 假设这个三方类有一个属性是 datasource
    @Bean
    public ThirdPartyClass thirdPartyClass() {
        ThirdPartyClass thirdPartyClass = new ThirdPartyClass();
        // 这里虽然是直接调用 setter 方法手动赋值，但是无论调用多少次 datasource() 方法其实返回都是 IoC 容器中的同一个对象，而不会创建多个对象
        thirdPartyClass.setDatasource(datasource());
        return thirdPartyClass;
    }

    // 我们需要在 Spring 配置类里创建这么一个 datasource 对象放到 IoC 容器中
    @Bean
    public Object datasource() {
        return new Object();
    }
}
