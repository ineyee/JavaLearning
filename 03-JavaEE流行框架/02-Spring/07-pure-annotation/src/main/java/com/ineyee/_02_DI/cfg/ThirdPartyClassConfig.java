package com.ineyee._02_DI.cfg;

import com.ineyee._02_DI.domain.ThirdPartyClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThirdPartyClassConfig {
    // 自定义属性，间接注入值
    @Value("22")
    Integer type;

    // 通过 @Bean 注解的方法来创建对象
    @Bean
    public ThirdPartyClass thirdPartyClass() {
        ThirdPartyClass thirdPartyClass = new ThirdPartyClass();
        // 通过 setter 方法把注入的值手动赋值
        thirdPartyClass.setType(type);
        return thirdPartyClass;
    }
}
