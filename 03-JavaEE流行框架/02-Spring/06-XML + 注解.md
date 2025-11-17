前面我们学习的一套是 Spring 1.x 时期的纯 XML 开发方式，纯 XML 开发方式存在以下问题：

* 大型项目 XML 上千行，巨大且难维护
* Java 类与配置分离，无法阅读代码就知道组件关系



所以 Spring 2.x 时期引入了注解开发方式来减少 XML、提升可读性，此时是 `XML + 注解混合开发方式，能用注解的尽量用注解，实在需要用 XML 的再用 XML`。

## 一、添加依赖

XML + 注解混合开发方式下，我们依然需要添加各个相关依赖

```xml
<!-- Spring 相关的依赖 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>7.0.0-RC1</version>
</dependency>
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

<!-- MyBatis 相关的依赖 -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.19</version>
</dependency>
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
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>6.1.1</version>
</dependency>

<!-- Spring 整合 MyBatis 特有的依赖 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>7.0.0-RC1</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>3.0.0</version>
</dependency>

<!-- 提供 Servlet 相关的接口和类 -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.1.0</version>
    <scope>provided</scope>
</dependency>

<!-- JSON 字符串和 Map 互转 -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.13.2</version>
</dependency>

<!-- 日志打印 -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.19</version>
</dependency>

<!-- 单元测试 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>
```

## 二、创建 Spring 配置文件和创建 applicationContext

XML + 注解混合开发方式下，我们依然需要创建 Spring 配置文件，也依然是通过 Spring 配置文件来创建 applicationContext

* 在 resources 目录下创建一个名字叫做 applicationContext.xml 的配置文件（这个文件名就用大驼峰，不要用中划线，因为是遗留下来的传统，很多 IDE 默认识别的就是这个名字），专门用来配置 Spring 相关的东西

```XML
<!-- applicationContext.xml -->

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
  
</beans>
```

* 在应用启动时通过 Spring 配置文件创建一次 applicationContext

```java
// App.java

public class App {
    public static void main(String[] args) {
        // 通过 Spring 配置文件来创建 applicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
```

## 三、注解实现 IoC

#### 1、基本实现

纯 XML 开发方式下，我们只要在 Spring 配置文件里写一个 <bean> 标签，Spring 框架就会自动创建一个对象并放到 IoC 容器里。

XML + 注解混合开发方式下，我们要想让 Spring 框架自动创建一个对象并放到 IoC 容器里，要做两件事：

* 在 Spring 配置文件里通过 context:component-scan 标签告诉 Spring 框架哪个包里的类是通过注解实现 IoC 的，Spring 框架就会扫描这个包里所有的类来自动创建对象并放到 IoC 容器里

  ```xml
  <!--
      通过 context:component-scan 标签告诉 Spring 框架哪个包里的类是通过注解实现 IoC 的
      Spring 框架就会扫描这个包里所有有注解的类来自动创建对象并放到 IoC 容器里
  -->
  <context:component-scan base-package="com.ineyee._01_IoC"/>
  ```

* 在`我们自己定义的 Java 类`里写一个 @Component 或 @Controller 或 @Service 或 @Repository 注解就可以了（@Controller、@Service、@Repository 和 @Component 的功能一模一样，只是为了见名知意名字不同），该对象默认的 beanId 就是它所属类名的第一个字母变成小写，当然我们也可以通过 value 属性来自定义 beanId（value 属性名可以省略不写，直接写值）

  ```java
  // User.java
  
  @Component
  public class User { }
  ```

  ```java
  // UserServlet.java
  
  @Controller
  public class UserServlet { }
  ```

  ```java
  // UserServiceImpl.java
  
  @Service(value = "userService")
  public class UserServiceImpl implements UserService { }
  ```

  ```java
  // UserDaoImpl.java
  
  @Repository("userDao")
  public class UserDaoImpl implements UserDao { }
  ```

> 现在我们就知道了两种 Spring 创建对象的方式，<bean> 标签法和 @Component 注解法
>
> 同时我们也会发现 @Component 注解法只适用于那些我们自己定义的 Java 类，因为只有这些 Java 类我们才能在源码里编写注解，而系统的类或三方库的类我们还是只能通过 <bean> 标签法来搞，因为我们不能在人家的源码里编写注解
>
> 换句话说，@Component 注解法只适用于我们自己定义的 Java 类，而 <bean> 标签法是万能的

#### 2、@Scope 注解

@Scope 注解就是之前 <bean> 标签里 scope 属性的功能，用来决定“在同一个 IoC 容器里 + 通过同一个 beanId 获取到的对象是不是只有一份”，默认值是 singleton，还有另外一个值是 prototype。

```java
@Component
@Scope("singleton")
//@Scope("prototype")
public class User { }
```

