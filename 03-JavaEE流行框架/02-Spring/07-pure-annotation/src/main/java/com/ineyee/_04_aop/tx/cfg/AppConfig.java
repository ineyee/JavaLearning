package com.ineyee._04_aop.tx.cfg;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.ineyee._04_aop.tx")
@EnableAspectJAutoProxy
public class AppConfig {
}
