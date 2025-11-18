package com.ineyee._01_IoC.cfg;

import com.ineyee._01_IoC.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan("com.ineyee._01_IoC")
public class AppConfig {
    @Bean("user")
    @Scope("singleton")
    //@Scope("prototype")
    public User user() {
        return new User();
    }
}
