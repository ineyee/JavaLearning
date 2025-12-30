# SSM 与 SpringBoot + MyBatis 依赖对比分析

## 一、SSM 开发项目时各层的依赖

### 1. **父项目(公共依赖)**
- `logback-classic` - 日志打印
- `junit-jupiter` - 单元测试

### 2. **dao 层依赖**
```xml
<!-- MyBatis 核心 -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
</dependency>

<!-- 数据库驱动 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>

<!-- 连接池 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
</dependency>

<!-- 分页插件 -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
</dependency>

<!-- Spring 整合 MyBatis 的桥梁 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
</dependency>

<!-- Spring 核心 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>

<!-- 参数校验 -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```

### 3. **service 层依赖**
```xml
<!-- Spring AOP(事务管理) -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
</dependency>

<!-- + dao 层所有依赖(通过依赖 dao 项目传递) -->
```

### 4. **controller 层依赖**
```xml
<!-- SpringMVC 核心 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
</dependency>

<!-- Servlet API -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
</dependency>

<!-- 参数校验 -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>

<!-- 文件上传 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-fileupload2-jakarta-servlet6</artifactId>
</dependency>

<!-- JSON 转换 -->
<dependency>
    <groupId>tools.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>

<!-- + service 层所有依赖(通过依赖 service 项目传递) -->
```

---

## 二、SpringBoot + MyBatis 开发项目时的依赖

SpringBoot 通过 **starter** 机制将多个依赖打包,大大简化了配置:

### 1. **核心 starter**
```xml
<!-- SpringBoot Web starter,包含了: -->
<!-- - spring-webmvc -->
<!-- - spring-context -->
<!-- - jackson-databind -->
<!-- - tomcat-embed (内嵌 Tomcat) -->
<!-- - spring-boot-starter (核心 starter) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- SpringBoot MyBatis starter,包含了: -->
<!-- - mybatis -->
<!-- - mybatis-spring -->
<!-- - spring-jdbc -->
<!-- - spring-boot-starter-jdbc (包含连接池 HikariCP) -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>

<!-- 数据库驱动(仍需单独引入) -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

### 2. **可选依赖**
```xml
<!-- AOP(SpringBoot 已内置,无需额外配置 aspectj) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

<!-- 参数校验 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- 分页插件(仍需单独引入) -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
</dependency>

<!-- 自定义连接池(如 Druid) -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
</dependency>

<!-- 测试 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 三、SSM 依赖如何合并到 SpringBoot + MyBatis 依赖中

| **SSM 依赖** | **合并到 SpringBoot Starter** | **说明** |
|-------------|---------------------------|---------|
| `spring-context` | `spring-boot-starter-web` | Spring 核心容器 |
| `spring-webmvc` | `spring-boot-starter-web` | SpringMVC 框架 |
| `jackson-databind` | `spring-boot-starter-web` | JSON 转换 |
| `jakarta.servlet-api` | `spring-boot-starter-web` (provided by Tomcat) | Servlet API |
| `mybatis` | `mybatis-spring-boot-starter` | MyBatis 核心 |
| `mybatis-spring` | `mybatis-spring-boot-starter` | MyBatis 与 Spring 整合 |
| `spring-jdbc` | `mybatis-spring-boot-starter` | Spring JDBC 支持 |
| `druid` | `mybatis-spring-boot-starter` 内置 HikariCP,若需 Druid 用 `druid-spring-boot-starter` | 连接池 |
| `aspectjrt` + `aspectjweaver` | `spring-boot-starter-aop` | AOP 支持 |
| `jakarta.validation-api` + `hibernate-validator` | `spring-boot-starter-validation` | 参数校验 |
| `logback-classic` | `spring-boot-starter` (内置 logback) | 日志 |
| `junit-jupiter` | `spring-boot-starter-test` (包含 JUnit 5) | 单元测试 |
| `commons-fileupload` | SpringBoot 使用 Servlet 3.0+ 标准文件上传,无需额外依赖 | 文件上传 |

---

## 四、关键变化总结

### 1. **不再分层管理依赖**
- **SSM**: 需要在 dao、service、controller 各层分别配置依赖
- **SpringBoot**: 通过 starter 统一管理,无需按层分别配置

### 2. **自动配置**
- **SSM**: 需要手动配置 `spring-context.xml`、`mybatis-config.xml`、`web.xml` 等大量 XML 配置文件
- **SpringBoot**: 通过 `application.yml` 或 `application.properties` 即可完成配置,大部分配置都有默认值

### 3. **内嵌服务器**
- **SSM**: 需要外部 Tomcat 服务器,需要打包成 war 并部署到 Tomcat
- **SpringBoot**: 内嵌 Tomcat,直接运行 main 方法启动,打包成 jar 即可运行

### 4. **版本管理**
- **SSM**: 需要手动管理每个依赖的版本,容易出现版本冲突
- **SpringBoot**: `spring-boot-dependencies` 统一管理所有依赖版本,避免冲突

### 5. **开发效率**
- **SSM**: 配置繁琐,需要理解 Spring、SpringMVC、MyBatis 三个框架的整合细节
- **SpringBoot**: 约定优于配置,开箱即用,专注业务开发

---

## 五、依赖数量对比

### SSM 项目依赖统计
- **dao 层**: 9 个依赖
- **service 层**: 2 个依赖 + dao 层依赖
- **controller 层**: 6 个依赖 + service 层依赖
- **总计**: 17+ 个显式依赖

### SpringBoot 项目依赖统计
- **核心依赖**: 3 个 starter (web + mybatis + mysql-connector)
- **可选依赖**: 按需添加 (aop、validation、pagehelper、druid 等)
- **总计**: 3-7 个显式依赖 (starter 内部自动管理了几十个传递依赖)

---

## 六、示例 pom.xml 对比

### SSM 项目完整依赖 (controller 层 pom.xml)
```xml
<dependencies>
    <!-- 通过依赖 service 项目,传递获得所有依赖 -->
    <dependency>
        <groupId>com.ineyee</groupId>
        <artifactId>service</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!-- SpringMVC 相关 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>7.0.1</version>
    </dependency>
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.1.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- 其他功能依赖... -->
</dependencies>
```

### SpringBoot 项目完整依赖
```xml
<dependencies>
    <!-- Web 开发 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- MyBatis 整合 -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
    </dependency>

    <!-- 数据库驱动 -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>

    <!-- 可选:参数校验 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

---

**总结**: SpringBoot 通过 starter 机制极大地简化了依赖管理,将 SSM 项目中分散在各层的 17+ 个依赖整合到了 3-7 个 starter 中,并提供了自动配置能力,大幅提升了开发效率。
