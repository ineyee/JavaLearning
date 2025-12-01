package com.ineyee._04_aop.basicuse.cfg;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.ineyee._04_aop.basicuse")
@EnableAspectJAutoProxy
public class AppConfig {
}
