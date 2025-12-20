package com.ineyee.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// Spring 主配置类
@Configuration
// 导入业务层配置类
@Import(ServiceConfig.class)
public class SpringConfig {
    // 这里面的配置都拆分到 DaoConfig 和 ServiceConfig 里了
}
