package com.ineyee.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// 自定义的属性绑定，以便一次性从 yml 配置文件里读取值注入到当前对象的属性上去，并且项目上线后能在 yml 配置文件里动态修改属性的值
@Component
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsProperties {
    private String[] allowedOrigins;
}
