## 一、SpringBoot + MongoDB 项目目录结构

```
├─${project-name}/(项目名)
│  ├─src/(项目的源文件)
│  │  ├─main/
│  │  │  ├─java/(我们编写的 Java 代码都放在这个文件夹里)
│  │  │  │  ├─com.ineyee/(公司唯一标识)
│  │  │  │  │  ├─common/(一些公用的东西)
│  │  │  │  │  │  ├─api/(给客户端响应数据和错误)
│  │  │  │  │  │  │  ├─error/(错误码和错误信息的枚举常量)
│  │  │  │  │  │  │  │  ├─ServiceError.java(父接口)
│  │  │  │  │  │  │  │  ├─CommonError implements ServiceError(通用错误码及错误信息)
│  │  │  │  │  │  │  ├─exception/(业务异常和全局异常处理)
│  │  │  │  │  │  │  │  ├─ServiceException.java(业务异常)
│  │  │  │  │  │  │  │  ├─GlobalExceptionHandler.java(全局异常处理)
│  │  │  │  │  │  │  ├─HttpResult.java(给客户端响应数据和错误的包装类)
│  │  │  │  │  │  │  ├─ListData.java(专门用来组装列表查询结果，返回给客户端)
│  │  │  │  │  │  ├─config/(配置类)
│  │  │  │  │  │  │  ├─CorsConfig.java(跨域处理相关配置)
│  │  │  │  │  │  │  ├─MongoConfig.java(MongoDB 相关配置)
│  │  │  │  │  │  ├─prop/(自定义的属性绑定)
│  │  │  │  │  │  │  ├─CorsProperties.java(跨域处理相关自定义的属性绑定)
│  │  │  │  │  ├─controller/(表现层之控制器层)
│  │  │  │  │  ├─pojo/(表现层之模型层)
│  │  │  │  │  │  ├──dto/
│  │  │  │  │  │  │  ├──UserDetailDto(用户详情业务响应给客户端的 dto)
│  │  │  │  │  │  │  ├──UserListDto(用户列表业务响应给客户端的 dto)
│  │  │  │  │  │  ├──document/
│  │  │  │  │  │  │  ├──User(用户文档，对应 MongoDB 中的 Document)
│  │  │  │  │  │  ├─query/
│  │  │  │  │  │  │  ├─ListQuery
│  │  │  │  │  │  │  ├─UserGetQuery(获取用户详情请求参数)
│  │  │  │  │  │  │  ├─UserListQuery extends ListQuery(获取用户列表请求参数)
│  │  │  │  │  │  ├─req/
│  │  │  │  │  │  │  ├─UserCreateReq(创建用户请求参数)
│  │  │  │  │  │  │  ├─UserCreateBatchReq(批量创建用户请求参数)
│  │  │  │  │  │  │  ├─UserDeleteReq(删除用户请求参数)
│  │  │  │  │  │  │  ├─UserDeleteBatchReq(批量删除用户请求参数)
│  │  │  │  │  │  │  ├─UserUpdateReq(更新用户请求参数)
│  │  │  │  │  │  │  ├─UserUpdateBatchReq(批量更新用户请求参数)
│  │  │  │  │  ├─repository/(数据层的接口，MongoDB 中叫 Repository)
│  │  │  │  │  ├─service/(业务层)
│  │  │  │  │  ├─Application.java(项目的入口类)
│  │  │  ├─resources/(我们编写的配置文件都放在这个文件夹里)
│  │  │  │  ├─static/(SpringBoot 项目的静态资源固定放在 static 目录下)
│  │  │  │  ├─application.yml(项目的主配置文件)
│  │  │  │  ├─application-dev.yml(项目的子配置文件、开发环境)
│  │  │  │  ├─application-prd.yml(项目的主配置文件、生产环境)
│  │  │  │  ├─logback-spring.xml(logger 的配置文件)
│  ├─target/(项目的打包产物)
│  ├─pom.xml(项目的配置文件，里面记录着项目的很多信息)
```

## 二、创建 yml 配置文件，Tomcat 部署配置

```yml
# application.yml

spring:
  profiles:
    # 通过子配置文件名来"引入、激活"子配置文件，这里是个数组
    # 开发环境用 dev，生产环境用 prd
    active:
      - dev
  mvc:
    servlet:
      # DispatcherServlet 的加载时机：默认是 -1（延迟加载，第一次请求接口时才初始化）
      # 设置为 >=0 表示在项目启动时就初始化 DispatcherServlet，数字越小优先级越高
      load-on-startup: 0

# MyBatisPlus 相关配置（MyBatis 相关配置转交给了 MyBatisPlus）
myBatis-plus:
  configuration:
    # 是否开启驼峰命名自动映射，即数据库表自动转 Java Bean 时是否从经典数据库列名 create_time 映射到经典 Java 属性名 createTime
    map-underscore-to-camel-case: true
  # type-aliases-package，用来给 xml 文件（如 mappers 里的 xml 文件、MyBatis 的配置文件等）里的类型自动取别名、短类名，如 type、parameterType、resultType 这种以 type 结尾的属性都是接收一个类型
  # 包名.类名，全类名，比较长；我们可以给全类名取个别名，短类名，比较短，写起来更方便；当然如果你偏好于写全类名，那也可以不定义别名
  type-aliases-package: com.ineyee.pojo
  # mapper 层实现的位置
  # 单表的 mapper 层实现，一般用 MyBatisPlus 自动生成的就够用了
  # 多表的 mapper 层实现，才需要像以前一样自定义 mapper 文件、自己去编写 SQL 语句（跟 MyBatisPlus 一起使用不会冲突）
  mapper-locations: classpath:mapper/*.xml
  global-config:
    db-config:
      # 主键生成策略：
      # MyBatisPlus 默认就是 ASSIGN_ID——雪花 ID，微服务、分布式时全局唯一。它会在 Java 代码里自动生成主键，此时我们就不需要设计主键为 AUTO_INCREMENT 了
      # 而单库单表时我们更推荐使用 AUTO——自增主键，性能和稳定性更好。是由数据库负责生成主键，此时我们就需要设计主键为 AUTO_INCREMENT 了
      id-type: ASSIGN_ID
      # 逻辑删除配置
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

# Swagger 相关配置，配置 swagger 框架的行为
springdoc:
  # 只扫描这些包下的接口
  packages-to-scan: com.ineyee.controller
```

```yaml
# application-dev.yml

# 服务器相关配置（SpringBoot 内置的 Web 容器 Tomcat）
# 假设在开发环境下端口号是 9999，Application Context Path 是 /tp-dev
server:
  # 监听的端口，默认是 8080
  port: 9999
  # Application Context Path，默认是 /，注意前面的 / 不能少，这个应用上下文就是 Tomcat 用来查找对应的项目的
  servlet:
    context-path: "/hmd-dev"

spring:
  # MySQL 数据源相关配置（数据库连接池、连接、数据库）
  # com.mysql.cj.jdbc.Driver：MySQL 数据库驱动库的类名
  # jdbc:mysql://：MySQL 的固定写法
  # localhost、3306：域名和端口号
  # db_hello_mysql：数据库的名称
  # serverTimezone=UTC：告诉数据库驱动 MySQL 已经把默认时区设置为了 0 时区
  # root、mysqlroot：数据库的用户名和密码
  # initialSize、maxActive：初始化创建几条连接、最大连接数
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC
    username: root
    password: mysqlroot
    druid:
      initial-size: 5
      max-active: 10
  # MongoDB 数据源相关配置
  # mongodb://：MongoDB 的固定写法
  # root、MongoDB666!：数据库的用户名和密码
  # localhost、27017：域名和端口号
  # test_db：数据库名
  # admin：认证用户是在哪个数据库里创建的
  # minPoolSize、maxPoolSize：连接池的最小连接数和最大连接数
  data:
    mongodb:
      uri: mongodb://root:MongoDB666!@localhost:27017/test_db?authSource=admin&minPoolSize=5&maxPoolSize=10

# 跨域处理配置，静态资源服务器的源白名单（cors 是我们自定义的属性绑定）
cors:
  allowed-origins:
    - http://127.0.0.1:5500
    - http://127.0.0.1:8888
```

```yaml
# application-prd.yml

# 服务器相关配置（SpringBoot 内置的 Web 容器 Tomcat）
# 假设在生产环境下端口号是 8888，Application Context Path 是 /tp
server:
  # 监听的端口，默认是 8080
  port: 8888
  # Application Context Path，默认是 /，注意前面的 / 不能少，这个应用上下文就是 Tomcat 用来查找对应的项目的
  servlet:
    context-path: "/hmd"

spring:
  # MySQL 数据源相关配置（数据库连接池、连接、数据库）
  # com.mysql.cj.jdbc.Driver：MySQL 数据库驱动库的类名
  # jdbc:mysql://：MySQL 的固定写法
  # 8.136.43.114、3306：域名和端口号
  # db_mysql：数据库的名称
  # serverTimezone=UTC：告诉数据库驱动 MySQL 已经把默认时区设置为了 0 时区
  # root、MySQLRoot666!：数据库的用户名和密码
  # initialSize、maxActive：初始化创建几条连接、最大连接数
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://8.136.43.114:3306/test_db?serverTimezone=UTC
    username: root
    password: MySQLRoot666!
    druid:
      initial-size: 5
      max-active: 10
  # MongoDB 数据源相关配置
  # mongodb://：MongoDB 的固定写法
  # root、MongoDB666!：数据库的用户名和密码
  # 8.136.43.114、27017：域名和端口号
  # test_db：数据库名
  # admin：认证用户是在哪个数据库里创建的
  # minPoolSize、maxPoolSize：连接池的最小连接数和最大连接数
  data:
    mongodb:
      uri: mongodb://root:MongoDB666!@8.136.43.114:27017/test_db?authSource=admin&minPoolSize=5&maxPoolSize=10

# 跨域处理配置，静态资源服务器的源白名单（cors 是我们自定义的属性绑定）
cors:
  allowed-origins:
    - http://8.136.43.114:5500
    - http://8.136.43.114:8888
```

## 三、编辑 pom.xml 文件，Maven 项目配置

```xml
<!-- pom.xml -->

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!--
        声明 pom.xml 文件的版本，就像 .html 文件里声明是 H5 那样，决定了该文件里能写什么标签不能写什么标签
        目前都使用 4.0.0，是必要元素
    -->
    <modelVersion>4.0.0</modelVersion>

    <!--
        groupId：com.ineyee，公司域名倒写，我们创建项目时填写的
        artifactId：默认就是项目名，我们创建项目时填写的
        version：1.0.0，项目版本号，我们创建项目时填写的
    -->
    <groupId>com.ineyee</groupId>
    <artifactId>hello_mongodb</artifactId>
    <version>1.0.0</version>

    <!--
        添加 parent 标签，让当前项目继承自 spring-boot-starter-parent 项目
        这个项目里已经帮我们锁定了一堆常用三方库的版本，从而避免开发中依赖间的版本冲突
    -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
    </parent>

    <!-- 所有的依赖 -->
    <dependencies>
        <!--
            添加一个 spring-boot-devtools 依赖，不用指定版本号，因为上面 spring-boot-starter-parent 项目里已经帮我们锁定好了
            这个库里已经帮我们添加了热部署功能，debug 模式下，它会监控 classpath 里字节码的变化（不是 target 里的），完成热部署
            也就是说，开发过程中，我们修改了代码，只需要重新 build 构建下项目，classpath 里字节码就会发生变化（不是 target 里的，所以只能用 IDEA 自带的 build，不能用 maven 的），就能完成热部署了，不用重新运行项目

            热部署 = 修改代码 + [fn cmd f9] 构建项目 + 查看效果
        -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>

        <!--
            属性绑定相关依赖：把 yml 配置文件里属性的值一次性注入到某个对象的属性上去
            在编译期间帮助生成 setter、getter、toString 等代码
        -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!--
            属性绑定相关依赖：把 yml 配置文件里属性的值一次性注入到某个对象的属性上去
            在 application-*.yml 配置文件里编写属性名时能提示
        -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
        </dependency>
    </dependencies>

    <!-- 属性信息 -->
    <properties>
        <!-- 告诉 Maven 编译、打包源码时使用 UTF-8，避免有些环境（如 Windows 服务器）使用系统默认的 GBK、ISO-8859-1 编码 -->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!-- 告诉 Maven 编译器插件，把源码编译成兼容 JDK8 的字节码文件 -->
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <!-- 构建信息，比如输出产物的名字、插件配置等 -->
    <build>
        <!-- 输出产物的名字 -->
        <finalName>hmd</finalName>
        <!-- 插件配置 -->
        <plugins>
            <!-- 开发结束后把项目打包成 runnable jar 的插件 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <!--
        打包方式，比如 jar、war 等
        如果不写这个标签，默认是打包成 jar
        之前没用 SpringBoot 的时候，需要打包成 war，然后部署在 Tomcat 上运行
        现在用了 SpringBoot，内置了 Tomcat，可以直接打包成 runnable jar 运行
    -->
    <packaging>jar</packaging>
</project>
```

## 四、添加依赖

> SpringBoot 官方提供的 starter 都是以 spring-boot-starter-xxx 开头，非 SpringBoot 官方提供的 starter 都是以 xxx-spring-boot-starter 结尾

#### 1、Spring、SpringMVC 相关依赖

- （必选）spring-boot-starter-web

```xml
<!--
    添加一个 spring-boot-starter-web 依赖，不用指定版本号，因为上面 spring-boot-starter-parent 项目里已经帮我们锁定好了
    这个库里已经帮我们添加了 Spring、SpringMVC 常用的依赖，还有 Tomcat 和日志打印相关依赖，就不用我们自己手动添加一大堆依赖了

    spring-context
    spring-webmvc
    spring-boot-starter-tomcat、jakarta.servlet-api
    jackson-databind
    logback-classic
-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- （可选）然后我们可以安装 Spring AOP 相关的库，以便使用 Spring 的面向切面编程往业务层里切入代码、如事务管理

```xml
<!--
    spring-boot-starter-aop
        aspectj
        aspectjweaver
-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

- （可选）SpringMVC 把所有参数都获取到一个请求参数模型里时，添加校验参数是否必传相关的库

```xml
<!--
    spring-boot-starter-validation
        jakarta.validation-api 是接口库
        hibernate-validator 是实现库
-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

#### 2、MyBatis、Spring 整合 MyBatis 相关依赖

* （必选）mybatis-spring-boot-starter

```xml
<!--
    添加一个 mybatis-spring-boot-starter 依赖，需要指定版本号，因为 mybatis-spring-boot-starter 是 MyBatis 官方提供的 starter，不是 SpringBoot 官方提供的 starter，所以 spring-boot-starter-parent 不会帮我们管理它的版本
    这个库里已经帮我们添加了 MyBatis、Spring 整合 MyBatis 常用的依赖，就不用我们自己手动添加一大堆依赖了

    mybatis
    mybatis-spring
    spring-jdbc
-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>
```

* （必选）然后我们知道 MyBatis 对应的是之前的 JDBC API，所以我们还需要安装数据库驱动、连接池

```xml
<!-- 可以不指定版本号，因为 MySQL 太常用了、spring-boot-starter-parent 会管理它的版本 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
<!-- 必须指定版本号，因为它是阿里巴巴的第三方库，SpringBoot 不管理它的版本 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.23</version>
</dependency>
```

* （可选）MyBatisPlus 相关的库，单表 CRUD 利器、多表 CRUD 不管

```xml
<!-- MyBatisPlus -->
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.5.15</version>
  <scope>compile</scope>
</dependency>
<!-- MyBatisPlus JSqlParser 依赖，3.5.9+ 版本需要单独引入才能使用分页插件 -->
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-jsqlparser</artifactId>
  <version>3.5.15</version>
</dependency>
```

#### 3、其它依赖

- （可选）单元测试

```xml
<!--
    单元测试：
    JUnit Jupiter（JUnit 5）
    Mockito（Mock 框架）
    AssertJ（断言库）
    Hamcrest（匹配器）
    Spring Test（Spring 测试支持）
-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

#### 4、MongoDB 相关依赖

```xml
<!-- MongoDB 相关依赖 -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

## 五、创建项目的入口类和入口方法，跟 pojo、controller、service、mapper 目录平级

```java
@EnableMongoAuditing  // 启用 MongoDB 审计功能，自动维护数据的创建时间和更新时间
// 创建项目的入口类，我们一般把它命名为 Application
//
// 用 @SpringBootApplication 注解修饰一下这个类，来标识它是项目的入口类
// 并且 @SpringBootApplication 注解还包含了 @ComponentScan 注解的功能，它默认的扫描路径就是当前类所在包及其子包下所有的类，扫描到用 @Component 注解修饰的类后就会自动创建对象并放到 IoC 容器中
// 所以 controller 层、service 层、其它目录里的众多类，都会被自动创建对象并放到 IoC 容器中
//
// mapper 层是通过 @MapperScan 注解来扫描的，Spring 会自动创建所有的 mapper 对象并放入 IoC 容器中，mapper 的扫描我们放到了 MyBatis-Plus 的配置类中
@SpringBootApplication
public class Application {
    // 为项目的入口类添加 main 方法，作为项目的入口方法
    public static void main(String[] args) {
        // 固定写法，启动项目
        SpringApplication.run(Application.class, args);
    }
}
```

## 六、SLF4J + Logback 日志系统搞一下

#### 1、添加 logger 的依赖

SpringBoot 项目的 spring-boot-starter-web 会默认添加 Logback 依赖，而 Logback 依赖会默认添加 SLF4J 依赖，所以我们不需要再手动添加。

#### 2、创建 logger 的配置文件

> 名字固定为 logback-spring.xml

```xml
<?xml version="1.0" encoding="utf-8" ?>
<!--
    scan="true" scanPeriod="30 seconds"
    让 logback 每隔 30 秒重新扫描一下配置文件，并应用最新的配置文件
-->
<configuration scan="true" scanPeriod="30 seconds">
    <!--
        使用 property 标签定义一个变量，抽取日志文件所在目录
            线上 Linux 系统，我们输出到 /var/log/${AppName}/app.log 文件中
    -->
    <property name="LOG_FILE_HOME" value="/var/log/test_project"/>

    <!--
        开发环境的 logger 配置
        application.yml 里的 active: - dev | - prd 会自动匹配这里的环境
    -->
    <springProfile name="dev">
        <!-- 定义一个输出目标：控制台 -->
        <appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
            <!-- 控制台的输出格式与编码方式 -->
            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <!--
                    输出格式
                        %highlight：彩色输出
                        %d：时间
                        %p：日志级别
                        %t：线程
                        %c：消息是在哪个类里输出的
                        %m：消息
                        %n：换行
                -->
                <pattern>%highlight([%d{yyyy-MM-dd HH:mm:ss.SSS}] [%-5p] [%t] [%c]: %m%n)</pattern>
                <!-- 编码方式：UTF-8 -->
                <charset>UTF-8</charset>
            </encoder>
        </appender>

        <!--
            项目里所有 logger 的配置都写在这个 root 标签里
                日志级别为：DEBUG
                是否给父 logger 传递日志事件：false
                输出目标为：控制台
        -->
        <root level="DEBUG" additivity="false">
            <appender-ref ref="consoleAppender"/>
        </root>
    </springProfile>

    <!--
        生产环境的 logger 配置
        application.yml 里的 active: - dev | - prd 会自动匹配这里的环境
    -->
    <springProfile name="prd">
        <!-- 定义一个输出目标：文件 -->
        <appender name="rollingFileAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <!-- 文件的输出格式与编码方式 -->
            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <!--
                    输出格式
                        %highlight：彩色输出，文件里面没有彩色
                        %d：时间
                        %p：日志级别
                        %t：线程
                        %c：消息是在哪个类里输出的
                        %m：消息
                        %n：换行
                -->
                <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%-5p] [%t] [%c]: %m%n</pattern>
                <!-- 编码方式：UTF-8 -->
                <charset>UTF-8</charset>
            </encoder>
            <!-- 文件的路径 -->
            <file>${LOG_FILE_HOME}/app.log</file>
            <!--
                文件的滚动策略，基于文件大小和时间
                    每隔一天，自动生成新文件，以当天日期命名
                    同一天内单个文件最大 100M，超过 100M 时自动生成新文件，以当天日期命名
                    所有文件总大小最大 10G，超过 10G 时自动清理最早的文件
                    自动清理超过 30 天的文件

                /var/log/${AppName}/
                ├─app.log                 <- 当前
                ├─app.log.2026.01.04.0    <- 今天的第 1 个文件（100M）
                ├─app.log.2026.01.04.1    <- 今天的第 2 个文件（100M）
                ├─app.log.2026.01.03.0    <- 昨天的文件
            -->
            <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                <!-- 自动生成的新文件名，时间精确到天，支持压缩文件（可节省 70%~90% 的存储空间） -->
                <fileNamePattern>${LOG_FILE_HOME}/app.log.%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
                <!-- 单个文件最大大小 -->
                <maxFileSize>100MB</maxFileSize>
                <!-- 所有文件总大小 -->
                <totalSizeCap>10GB</totalSizeCap>
                <!-- 最多保留多少天，这里的单位取决于 fileNamePattern 里的时间精确到什么单位 -->
                <maxHistory>30</maxHistory>
            </rollingPolicy>
        </appender>

        <!-- 把 rollingFileAppender 搞成异步的，这样一来把日志写入到文件的操作就会变成异步的 -->
        <appender name="asyncRollingFileAppender" class="ch.qos.logback.classic.AsyncAppender">
            <!-- 引用上面定义的 rollingFileAppender -->
            <appender-ref ref="rollingFileAppender"/>
        </appender>

        <!--
            项目里所有 logger 的配置都写在这个 root 标签里
                日志级别为：INFO
                是否给父 logger 传递日志事件：false
                输出目标为：文件
        -->
        <root level="INFO" additivity="false">
            <appender-ref ref="asyncRollingFileAppender"/>
        </root>
    </springProfile>
</configuration>
```

#### 3、编写 Java 代码，输出日志

> 实际开发中，建议为每个类写一个独立的 logger，而不是整个项目只写一个全局的 logger。因为“整个项目只写一个全局的 logger”无法区分日志来源于哪个包哪个类哪行代码、无法按包或按类进行日志级别控制

```java
package com.ineyee.controller;

import com.ineyee.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    // LoggerFactory.getLogger 的时候把当前类传进去，就能在日志中看到当前类名
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public String test() {
        logger.debug("我是调试_DEBUG");
        logger.info("我是信息_INFO");
        logger.warn("我是警告_WARN");
        logger.error("我是错误_ERROR");

        testService.test();

        return "test";
    }
}
```

```java
package com.ineyee.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// 并且如果我们的项目里使用了 lombok
// 那么只要用 @Slf4j 注解修饰一下类，lombok 就会自动创建一个变量名为 log 的静态 logger 对象
// 这样一来我们就不用在每个类里手动创建 logger 对象了，直接输出日志即可
@Slf4j
@Service
public class TestService {
    public void test() {
        log.debug("我是调试_DEBUG");
        log.info("我是信息_INFO");
        log.warn("我是警告_WARN");
        log.error("我是错误_ERROR");
    }
}
```

## 七、common 目录里的东西

common 目录里的东西基本都是固定的，可以直接拷贝一份到项目里，后续再根据实际业务做扩展。

## 八、MongoDB CRUD 操作（以 product 集合为例）

> - 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> - 一个数据库里可以有多张表，比如 user、product 这两张表
> - 一张表对应一组 mapper、service、pojo、controller，比如 UserMapper、UserService、UserXxo、UserController、ProductMapper、ProductService、ProductXxo、ProductController 这两组

#### 1、表现层之模型层 pojo

###### 1.1 响应体模型 po、dto

> 之前使用 MySQL 的时候，我们会为每张表都创建一个对应的 po、po 肯定是某张表在 Java 代码里的投影，所以 po 和表肯定是一对一的。这主要是因为 MySQL 里对象和其它对象的嵌套关系都是通过多表关联实现的，所以其它对象都有自己独立的表
>
> 但是现在使用 MongoDB 的时候，MongoDB 里的对象可以通过 map 或数组直接嵌套其它对象，其它对象不需要有自己独立的集合，所以我们同样会为每个集合都创建一个对应的 po、并且还得为嵌套对象创建对应的 po，所以 po 和集合就不是一对一了，po 数会多于集合数，即一个集合肯定对应着一个 po、但一个 po 不一定有对应的集合

我们会固定为每个集合生成一个 po，以及 po 对应的 detailDto、listDto，当然后续可以根据业务需要自行创建更多的业务 dto，因为**我们响应给客户端的模型起码得是 dto 起步、最好不要直接返回 po，返回 dto 方便以后扩展**：

- Product 这个 po 是对数据库表字段的映射
- ProductDetailDto 这个 dto 默认是把 po 里的字段全都拿过来了，供 get 详情接口的返回值使用，所以字段应该尽可能地全。**根据实际情况删掉不应该返回给客户端的敏感字段、比如 deleted 软删除字段，这里 po 到 detailDto 的转换是在 service 层完成的**
- ProductListDto 这个 dto 默认一个字段也没有，供 list 列表接口返回值使用，所以字段应该尽可能地少。**根据实际情况从 po 里筛选需要的字段加进来，这里 po 到 listDto 的转换是在 service 层完成的**

###### 1.2 请求参数模型 query、req

我们会固定为 get 接口、list 接口、save 接口、saveBatch 接口、remove 接口、removeBatch 接口、update 接口、updateBatch 接口创建对应的请求参数模型，当然后续可以根据接口需要自行创建更多的请求参数模型：

- ProductGetQuery 这个 query 默认只有一个必选参数 id，代表通过 id 查询详情
- ProductListQuery 这个 query 默认有分页查询参数、模糊搜索参数，但都是可选参数。**可以根据实际情况去添加各种业务上的查询参数**
- ProductCreateReq 这个 req 默认是把 po 里能自动填充（id、create_time、update_time、deleted）以外的所有字段全都拿过来了。**可以根据实际情况去修改，并添加字段是否必传的注解**
- ProductCreateBatchReq 这个 req 默认是 ProductCreateReq 的数组，必选参数。**可以不用动，只需要维护好 ProductCreateReq 的字段即可**
- ProductDeleteReq 这个 req 默认只有一个必选参数 id，代表通过 id 删除
- ProductDeleteBatchReq 这个 req 默认是 ProductDeleteReq 的数组，必选参数。**可以不用动，只需要维护好 ProductDeleteReq 的字段即可**
- ProductUpdateReq 这个 req 默认是把 po 里能自动填充（id、create_time、update_time、deleted）以外的所有字段全都拿过来了 + 一个必选参数 id。**可以根据实际情况去修改，并添加字段是否必传的注解**
- ProductUpdateBatchReq 这个 req 默认是 ProductUpdateReq 的数组，必选参数。**可以不用动，只需要维护好 ProductUpdateReq 的字段即可**

#### 2、数据层 repository

spring-boot-starter-data-mongodb 提供了 MongoRepository 接口，类似于 MyBatisPlus 的 BaseMapper 接口，提供了【增删改 + 基本查】方法来访问 MongoDB 数据库，尽量优先用 repository 来访问数据库，复杂操作再用 mongoTemplate 来访问数据库。

#### 3、业务层 service

自动注入 repository 对象、mongoTemplate 对象，调用数据层的 api 即可。

#### 4、表现层之控制器层 controller

自动注入 service 对象，调用业务层的 api 即可。







  ```java
  九、MongoDB 高级特性

  9.1 索引创建

  package com.ineyee.pojo.document;

  import lombok.Data;
  import org.springframework.data.annotation.Id;
  import org.springframework.data.mongodb.core.index.Indexed;
  import org.springframework.data.mongodb.core.index.CompoundIndex;
  import org.springframework.data.mongodb.core.mapping.Document;

  @Data
  @Document(collection = "user")
  @CompoundIndex(name = "name_age_idx", def = "{'name': 1, 'age': -1}")  // 复合索引
  public class User {
      @Id
      private String id;

      @Indexed(unique = true)  // 唯一索引
      private String email;

      @Indexed  // 普通索引
      private String name;

      private Integer age;
  }

  9.2 聚合查询

  @Service
  public class UserServiceImpl implements UserService {

      @Autowired
      private MongoTemplate mongoTemplate;

      public List<AgeStatDto> getAgeStatistics() {
          // 聚合管道
          Aggregation aggregation = Aggregation.newAggregation(
              Aggregation.match(Criteria.where("deleted").is(false)),
              Aggregation.group("age").count().as("count"),
              Aggregation.sort(Sort.Direction.DESC, "count")
          );

          AggregationResults<AgeStatDto> results = mongoTemplate.aggregate(
              aggregation, "user", AgeStatDto.class
          );

          return results.getMappedResults();
      }
  }

  9.3 事务支持

  MongoDB 4.0+ 支持多文档事务：

  @Service
  public class UserServiceImpl implements UserService {

      @Autowired
      private MongoTemplate mongoTemplate;

      @Transactional  // 需要配置事务管理器
      public void transferOperation() {
          // 多个操作在同一个事务中
          User user1 = mongoTemplate.findById("id1", User.class);
          User user2 = mongoTemplate.findById("id2", User.class);

          // 修改操作
          mongoTemplate.save(user1);
          mongoTemplate.save(user2);
      }
  }

  需要在配置类中启用事务：

  @Configuration
  @EnableMongoRepositories(basePackages = "com.ineyee.repository")
  @EnableTransactionManagement
  public class MongoConfig {

      @Bean
      public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
          return new MongoTransactionManager(dbFactory);
      }
  }
  ```