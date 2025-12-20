本篇我们用 SSM 整合实现一下《02-JavaWeb基础：05-ProjectArchitecture》，基于纯注解

## ✅ 一、项目目录结构划分

```
├─${project-name}/(项目名)
│  ├─src/(项目的源文件)
│  │  ├─main/
│  │  │  ├─java/(我们编写的 Java 代码都放在这个文件夹里)
│  │  │  │  ├─com.ineyee/(公司唯一标识)
│  │  │  │  │  ├─cfg/(之前的 xml 文件现在都变成了 Java 配置类)
│  │  │  │  │  │  ├─WebInitializer.java(对应之前的 web.xml 配置文件)
│  │  │  │  │  │  ├─SpringConfig.java(对应之前的 applicationContext.xml 配置文件)
│  │  │  │  │  │  ├─SpringMVCConfig.java(对应之前的 dispatchServlet.xml 配置文件)
│  │  │  │  │  ├─api/(给客户端响应数据和错误)
│  │  │  │  │  │  ├─HttpResult.java(给客户端响应数据和错误的包装类)
│  │  │  │  │  │  ├─error/(错误码和错误信息的枚举常量)
│  │  │  │  │  │  │  ├─Error.java(父接口)
│  │  │  │  │  │  │  ├─CommonError implements Error(通用错误码及错误信息)
│  │  │  │  │  │  │  ├─UserError implements Error(用户模块错误码及错误信息)
│  │  │  │  │  │  ├─exception/(业务异常和全局异常处理)
│  │  │  │  │  │  │  ├─ServiceException.java(业务异常)
│  │  │  │  │  │  │  ├─GlobalExceptionHandler.java(全局异常处理)
│  │  │  │  │  ├─domain/(表现层之模型层)
│  │  │  │  │  │  ├─BaseDomain
│  │  │  │  │  │  ├─User extends BaseDomain
│  │  │  │  │  ├─dao/(数据层的接口)
│  │  │  │  │  │  ├─UserDao
│  │  │  │  │  ├─service/(业务层)
│  │  │  │  │  │  ├─UserService
│  │  │  │  │  │  ├─UserServiceImpl implements UserService
│  │  │  │  │  ├─controller/(表现层之控制器层)
│  │  │  │  │  │  ├─UserController
│  │  │  │  │  ├─dto/(接收客户端的请求参数)
│  │  │  │  │  │  ├─UserSaveDto
│  │  │  │  │  │  ├─UserRemoveDto
│  │  │  │  │  │  ├─UserUpdateDto
│  │  │  │  │  │  ├─UserGetDto、UserListDto
│  │  │  │  │  ├─interceptor/(各种拦截器)
│  │  │  │  │  │  ├─HttpLogInterceptor(HTTP 请求日志拦截器)
│  │  │  │  │  ├─filter/(各种过滤器)
│  │  │  │  │  │  ├─CachedBodyFilter(HTTP 请求体缓存过滤器)
│  │  │  ├─resources/(我们编写的配置文件都放在这个文件夹里，如 .properties、.xml 文件)
│  │  │  │  ├─mappers/(数据层的实现)
│  │  │  │  │  ├─user.xml
│  │  │  │  ├─applicationContext.xml(Spring 的主配置文件)
│  │  │  │  ├─dispatcherServlet.xml(Spring 的子配置文件，for SpringMVC)
│  │  │  │  ├─dao.properties(数据层 dao 相关配置的值都写在这个配置文件里)
│  ├─target/(项目的打包产物)
│  ├─pom.xml(项目的配置文件，里面记录着项目的很多信息)
```

## ✅ 二、Tomcat 部署配置

- 在 Current File - Edit Configurations - Add New - Tomcat Server - Local - IDE 会自动检测到我们安装在本机的 Tomcat，大多数配置我们不需要修改，只需要修改部分配置
- Server - HTTP port，由默认的 8080 换成自定义的端口比如 9999（本机上可能有其它软件已经在监听 8080 端口了，会导致 Tomcat 无法监听；${TOMCAT_HOME}/conf/server.xml 文件，<Connector> 标签里也能修改端口）
- Deployment - Add - Artifact - ${项目名}:war exploded 或 ${项目名}:war
  - ${项目名}:war exploded 代表不打包成 war 包，直接把项目以展开的文件夹形式部署到 Tomcat 上去，适用于开发阶段。也就是说我们在电脑上开发项目的时候适合用这个，因为开发阶段我们要频繁修改代码重新部署来看效果，这样一来就不用每次都打成压缩包，效率更快，换言之就是热部署效率更高
  - ${项目名}:war 代表把项目打包成 war 包，再部署到 Tomcat 上去，适用于发布阶段。也就是说我们在部署到正式或测试服务器的时候适合用这个，因为发布阶段用压缩包，文件传输速率和文件完整性都更容易得到保证
- `把下面的 Application context 由 “/12_ssm_xml_annotation_war_exploded”或”/12_ssm_xml_annotation_war“这么长的一串换成自定义的“/ssm”（注意前面的 / 不能少，这个应用上下文就是 Tomcat 用来查找对应的项目的）`
- Apply - OK

## ✅ 三、Maven 项目配置

```xml
<!-- pom.xml -->

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
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
    <artifactId>12-ssm-xml-annotation</artifactId>
    <version>1.0.0</version>

    <!-- 所有的依赖 -->
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
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
        <finalName>ssm</finalName>
        <!-- 插件配置 -->
        <plugins>
        </plugins>
    </build>

    <!--
      打包方式，比如 jar、war 等
      如果不写这个标签，默认是打包成 jar
      如果是个 JavaWeb 项目，那应该打包成 war
    -->
    <packaging>war</packaging>
</project>
```

## ✅ 四、添加依赖

#### 1、Spring 相关依赖（串联各个层）

* （必选，一定要跟 SpringMVC 的版本一致）首先安装 Spring

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>7.0.1</version>
</dependency>
```

* （可选）然后我们可以安装 Spring AOP 相关的库，以便使用 Spring 的面向切面编程往业务层里切入代码、如事务管理

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.9.25</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.25</version>
    <scope>runtime</scope>
</dependency>
```

#### 2、SpringMVC 相关依赖（controller 层）

* （必选，一定要跟 Spring 的版本一致）首先安装 SpringMVC

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>7.0.1</version>
</dependency>
```

* （必选）然后我们知道 SpringMVC 是对 Servlet API 的封装，所以我们还需要安装 jakarta.servlet-api

```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.1.0</version>
    <scope>provided</scope>
</dependency>
```

* （可选）然后我们可以安装 Java 对象或 Map 和 Json 字符串互转的库，以便 SpringMVC 能完成“请求体的 Json 字符串自动转 Java 对象或 Map”、“响应体的 Java 对象或 Map 自动转 Json 字符串”

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

* （可选）然后我们可以安装一个数据类型转换库，只要我们安装了这个库 Jackson 就会自动检测并注册 JavaTimeModule，就能完成数据库里日期 DATETIME 和 Java 代码里 LocalDateTime 类型的转换（还需要在 SpringMVC 配置文件里设置一下 LocalDateTime 的格式）

```xml
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-jsr310</artifactId>
  <version>2.15.2</version>
</dependency>
```

* （可选）把所有参数都获取到一个请求参数模型里时，添加校验参数是否必传相关的库，jakarta.validation-api 是接口，hibernate-validator 是具体实现

```xml
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.0.Final</version>
</dependency>
```

* （可选）然后我们可以安装文件上传库，以便 SpringMVC 能帮我们非常简单地实现文件上传

```xml
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-fileupload2-jakarta-servlet6</artifactId>
  <version>2.0.0-M4</version>
</dependency>
```

#### 3、MyBatis 相关依赖（dao 层）

* （必选）首先安装 MyBatis

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.19</version>
</dependency>
```

* （必选）然后我们知道 MyBatis 对应的是之前的 JDBC API，所以我们还需要安装数据库驱动、连接池

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>9.4.0</version>
</dependency>
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>druid</artifactId>
  <version>1.2.27</version>
</dependency>
```

* （可选）然后我们可以安装分页查询库，以便 MyBatis 能用一份 SQL 语句应对不同的数据库如 MySQL、Oracle、PostgreSQL

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>6.1.1</version>
</dependency>
```

#### 4、Spring 整合 MyBatis 特有的依赖

* （必选）Spring 提供了一个

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-jdbc</artifactId>
  <version>7.0.0-RC1</version>
</dependency>
```

* （必选）MyBatis 提供了一个

```xml
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis-spring</artifactId>
  <version>3.0.5</version>
</dependency>
```

#### 5、其它依赖

* （可选）日志打印

```xml
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.5.19</version>
</dependency>
```

* （可选）单元测试

```xml
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <version>5.8.2</version>
  <scope>test</scope>
</dependency>
```

## ✅ 五、在 WebInitializer 里做一些配置

> 这是非常老旧的配置方法，需要我们手动将 DispatcherServlet 的配置写入 WebInitializer 类里，这里仅做演示用
>
> Spring Boot 诞生后，在检测到 Spring MVC 的依赖后，会自动地、隐式地为我们配置好 DispatcherServlet，其默认拦截路径就是 "/"，让开发者能专注于写接口的业务代码，这个后面再说

> 整体工作流程总结：
>
> * JavaWeb 项目在 Tomcat 等 Servlet 容器中启动
> * 容器会读取项目中哪个类继承自 AbstractAnnotationConfigDispatcherServletInitializer 类，就会找到我们自己创建的  WebInitializer 类，创建并初始化 DispatcherServlet
> * DispatcherServlet 会根据配置加载主配置类和子配置类，创建 Spring 容器及各个 controller 层的 bean 对象
> * 此后，所有来自客户端的请求都会首先经过 DispatcherServlet
> * DispatcherServlet 会根据请求路径和请求方法，找到 @Controller 中具体的方法执行，最后返回响应给客户端

之前 XML + 注解我们是在 web.xml 里做一些配置，现在纯注解我们是在 WebInitializer 类里做一些配置，有了这个类，我们就可以把 web.xml 删掉了，它俩是同样的用途：

```java
// 创建一个 WebInitializer 类，继承 AbstractAnnotationConfigDispatcherServletInitializer 类
// 重写若干方法，在这些方法里做配置
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    // 1、指定 Spring 主配置类
    @Override
    protected Class<?> @Nullable [] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    // 2、指定 Spring 子配置类，for SpringMVC
    @Override
    protected Class<?> @Nullable [] getServletConfigClasses() {
        return new Class[]{SpringMVCConfig.class};
    }

    // 配置主控制器可以拦截哪些请求，注意还需配合 SpringMVC 配置类里的 configureDefaultServletHandling 方法一起使用
    // 将 DispatcherServlet 的拦截模式设置为 "/"，这意味着 DispatcherServlet 会拦截接口型请求，会拦截静态资源型请求，不会拦截动态资源型请求（拦截 2 个）
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /*
     * 3、配置过滤器
     * 相当于 XML 里的 <filter> + <filter-mapping>
     *
     * 配置了两个过滤器：
     * 1. CharacterEncodingFilter: 字符编码过滤器，用来处理 HTTP 请求的字符编码
     * 2. CachedBodyFilter: 请求体缓存过滤器，用来包装请求使请求体可以被多次读取
     *
     * 过滤器会自动拦截所有请求（/* 模式），包括：接口型请求 + 静态资源请求 + 动态资源请求
     * 过滤器的执行顺序：数组中的顺序就是执行顺序
     */
    @Override
    protected Filter[] getServletFilters() {
        // 1、创建字符编码过滤器
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        // 设置编码为 UTF-8，UTF-8 是目前最通用的字符编码
        characterEncodingFilter.setEncoding("UTF-8");
        // 强制使用 UTF-8 编码来处理请求和响应
        // 相当于 XML 里的 <init-param><param-name>forceEncoding</param-name><param-value>true</param-value></init-param>
        characterEncodingFilter.setForceEncoding(true);

        // 2、创建请求体缓存过滤器
        // 因为 HTTP 请求体只能被读取一次，拦截器读取后 Controller 就无法读取了
        // 所以需要用这个过滤器在拦截器之前包装请求，使请求体可以被多次读取
        CachedBodyFilter cachedBodyFilter = new CachedBodyFilter();

        // 返回过滤器数组，执行顺序：characterEncodingFilter -> cachedBodyFilter
        return new Filter[]{characterEncodingFilter, cachedBodyFilter};
    }

    // 4、配置文件上传，相当于 XML 里的 <multipart-config>
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        /*
         * 创建 MultipartConfigElement 对象，配置文件上传参数
         *
         * 参数说明：
         * 1. location: 文件上传时的临时目录，默认是 Servlet 容器的临时目录
         *    - 文件上传时，数据会先写入到硬盘上的临时目录，而不是直接放到内存中
         *    - 这样可以防止上传大文件时占用过多内存
         *    - 上传完成后，再将文件移动到指定的目标目录
         *
         * 2. maxFileSize: 单个文件的最大字节数
         *    - 默认值是 -1，表示无限制
         *    - 如果上传的文件超过该大小限制，会抛出异常
         *    - 这里设置为 10485760 字节，即 10 MB
         *
         * 3. maxRequestSize: 一次上传请求中所有文件的总大小上限
         *    - 默认值是 -1，表示无限制
         *    - 如果上传的文件总大小超过该限制，会抛出异常
         *    - 这里设置为 20971520 字节，即 20 MB
         *
         * 4. fileSizeThreshold: 文件写入到临时文件之前，允许保存在内存中的临界值
         *    - 当文件大小超过该值时，才会写入硬盘临时目录；否则保存在内存中
         *    - 默认是 0，表示所有上传文件都直接写入硬盘临时目录
         */
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                "/tmp",           // location: 临时目录
                10485760,         // maxFileSize: 单个文件最大 10 MB
                20971520,         // maxRequestSize: 请求总大小最大 20 MB
                0                 // fileSizeThreshold: 内存临界值 0 字节
        );

        // 将文件上传配置应用到 DispatcherServlet
        registration.setMultipartConfig(multipartConfig);
    }
}
```

## ✅ 六、创建 Spring 的配置类，做一些配置

之前 XML + 注解我们是在 applicationContext.xml 主配置文件和 dispatcherServlet.xml 子配置文件里做一些配置，现在纯注解我们是在  SpringConfig 主配置类和 SpringMVCConfig 子配置类里做一些配置，它们是同样的用途：

* SpringConfig 主配置类

```java
// Spring 主配置类
@Configuration
// 通过 @ComponentScan 注解告诉 Spring 框架哪个包里的类是通过注解实现 IoC 的
// Spring 框架就会扫描这个包里所有有注解的类来自动创建对象并放到 IoC 父容器里
//
// 父容器只需要扫描 service 所在的包即可
@ComponentScan("com.ineyee.service")
public class SpringConfig { }
```

* SpringMVCConfig 子配置类

```java
// Spring 子配置类，for SpringMVC，实现 WebMvcConfigurer 接口
// 实现若干方法，在这些方法里做配置
@Configuration
// 使用注解来开发 SpringMVC 的话，得加上这个注解
@EnableWebMvc
/*
通过 @ComponentScan 注解告诉 SpringMVC 框架哪个包里的 Controller 类是通过注解实现 IoC 的
SpringMVC 框架就会扫描这个包里所有有注解的类来自动创建对象并放到 IoC 子容器里

子容器需要扫描：
    1. controller 所在的包
    2. 全局异常处理器所在的包（@ControllerAdvice）
    3. 拦截器所在的包
 */
@ComponentScan("com.ineyee.controller")
@ComponentScan("com.ineyee.api.exception")
@ComponentScan("com.ineyee.interceptor")
public class SpringMVCConfig implements WebMvcConfigurer {
    /*
    相当于 XML 里 <mvc:default-servlet-handler/> + <mvc:annotation-driven/> 的用途

    DispatcherServlet 虽然拦截到了静态资源
    但是我们不让它处理，而是转交给默认的静态资源 Servlet 走服务器默认的处理
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
```

## ✅ 七、api 目录里的东西

api 目录里的东西基本都是固定的，可以直接拷贝一份到项目里，后续再根据实际业务做扩展。

## ✅ 八、表现层之模型层 domain

> * 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> * 一个数据库里可以有多张表，比如 user、product 这两张表
> * 一张表对应一组 dao、service、domain、controller，比如 UserDao、UserService、User、UserController、ProductDao、ProductService、Product、ProductController 这两组

纯粹地存储数据，domain 的字段必须和数据库表里的字段一一对应。

## ✅ 九、数据层 dao

> * 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> * 一个数据库里可以有多张表，比如 user、product 这两张表
> * 一张表对应一组 dao、service、domain、controller，比如 UserDao、UserService、User、UserController、ProductDao、ProductService、Product、ProductController 这两组

#### 1、Java 代码

先定义一个 dao 接口，然后再定义一个 mapper 文件、这个 mapper 文件其实就是 dao 接口的实现。

#### 2、配置

首先创建一个 dao.properties 文件，把数据层 dao 相关配置的值都写在这个配置文件里。

然后去 Spring 主配置类里配置一堆数据层 dao 的相关配置（当然我们也可以抽取成单独的配置类来为主配置类减负）。

## ✅ 十、业务层 service

> * 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> * 一个数据库里可以有多张表，比如 user、product 这两张表
> * 一张表对应一组 dao、service、domain、controller，比如 UserDao、UserService、User、UserController、ProductDao、ProductService、Product、ProductController 这两组

#### 1、Java 代码

先定义一个 service 接口，然后再定义一个 service 接口的实现类、用 @Service 修饰一下放入父 IoC 容器里、自动注入 dao。

#### 2、配置

去 Spring 主配置类里配置一堆业务层 service 的相关配置（当然我们也可以抽取成单独的配置类来为主配置类减负）。

## ✅ 十一、控制器层 controller

> * 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> * 一个数据库里可以有多张表，比如 user、product 这两张表
> * 一张表对应一组 dao、service、domain、controller，比如 UserDao、UserService、User、UserController、ProductDao、ProductService、Product、ProductController 这两组

#### 1、Java 代码

定义一个 controller 类、用 @Controller 修饰一下放入子 IoC 容器里、自动注入 service。

#### 2、配置

去 Spring 子配置文件里配置一堆控制器层 controller 的相关配置。
