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

#### 基本实现

> 现在我们就知道了两种 Spring 创建对象的方式，<bean> 标签法和 @Component 注解法
>
> 同时我们也会发现 @Component 注解法只适用于那些我们自己定义的 Java 类，因为只有这些 Java 类我们才能在源码里编写注解，而系统的类或三方库的类我们还是只能通过 <bean> 标签法来搞，因为我们不能在人家的源码里编写注解
>
> 换句话说，@Component 注解法只适用于我们自己定义的 Java 类，而 <bean> 标签法是万能的

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

#### 补充：@Scope 注解

@Scope 注解就是之前 <bean> 标签里 scope 属性的功能，用来决定“在同一个 IoC 容器里 + 通过同一个 beanId 获取到的对象是不是只有一份”，默认值是 singleton，还有另外一个值是 prototype。

```java
@Component
@Scope("singleton")
//@Scope("prototype")
public class User { }
```

## 四、注解实现 DI

#### 基本实现

> 现在我们就知道了两种 Spring 依赖注入的方式，<property> 标签法和 @Autowired、@Value 注解法
>
> 同时我们也会发现 @Autowired、@Value 注解法只适用于那些我们自己定义的 Java 类 + 基本数据类型、BigDecimal、字符串、自定义对象类型的属性，因为只有这些 Java 类我们才能在源码里编写注解，而系统的类或三方库的类我们还是只能通过 <bean> 标签法来搞，因为我们不能在人家的源码里编写注解，并且
>
> 换句话说，@Autowired、@Value 注解法只适用于我们自己定义的 Java 类 + 基本数据类型、BigDecimal、字符串、自定义对象类型的属性，而 <property> 标签法是万能的

实际开发中，一般都是通过 setter 方法注入，所以这里就不演示通过构造方法注入了。

纯 XML 开发方式下，我们是在 Spring 配置文件里的 <bean> 标签里嵌套 <property> 标签来实现依赖注入的：

* 当属性的数据类型为基本数据类型、BigDecimal、字符串时，直接使用 <property> 标签的 value 属性给属性赋值即可
* 当属性的数据类型为自定义对象类型时，直接使用 <property> 标签的 ref 属性给属性赋值即可
* 当属性的数据类型为集合类型时，需要使用特定的集合标签给属性赋值

XML + 注解混合开发方式下，我们要实现依赖注入，直接在`我们自己定义的 Java 类`里的 setter 方法上写一个 @Value 或 @Autowired 注解就可以了，其中：

* 当属性的数据类型为基本数据类型、BigDecimal、字符串时，用 @Value（@Value 注入的值总是一个字符串，会自动转换成目标数据类型）
* 当属性的数据类型为自定义对象类型，用 @Autowired（@Autowired 是根据 setter 方法参数的数据类型注入的，而不是参数的名字）
* ~~当属性的数据类型为集合类型时，我们还是只能通过 <bean> 标签法来搞~~

```java
// Dog.java

@Component
public class Dog {
    private String name;

    @Value("旺财")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
```

```java
// Person.java

@Component
public class Person {
    // 基本数据类型
    private Boolean isPartyMember;
    private Integer age;
    private Double height;

    @Value("false")
    public void setPartyMember(Boolean partyMember) {
        isPartyMember = partyMember;
    }

    @Value("18")
    public void setAge(Integer age) {
        this.age = age;
    }

    @Value("1.88")
    public void setHeight(Double height) {
        this.height = height;
    }

    // BigDecimal
    private BigDecimal salary;

    @Value("666666.66")
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    // 字符串
    private String name;

    @Value("张三")
    public void setName(String name) {
        this.name = name;
    }

    // 自定义对象类型
    private Dog dog;

    @Autowired
    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "isPartyMember=" + isPartyMember +
                ", age=" + age +
                ", height=" + height +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                ", dog=" + dog +
                '}';
    }
}
```

#### 补充：@Qualifier 注解

上面我们已经提到“@Autowired 是根据 setter 方法参数的数据类型注入的，而不是参数的名字”，那么如果 IoC 容器里有多个 UserService 类的实现，比如：

```java
public interface UserService { }

@Service(value = "userService")
public class UserServiceImpl implements UserService { }

@Service(value = "userService1")
public class UserServiceImpl1 implements UserService { }
```

此时如果仅靠 @Autowired 类型注入就会报错：NoUniqueBeanDefinitionException，因为 Spring 不知道我们到底要用哪一个实现类的对象：

```java
@Controller
public class UserServlet {
    private UserService userService;

    @Autowired()
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

这时就得用 @Qualifier 注解来告诉 Spring，我们明确想要用 beanId 为 xxx 的那一个bean 对象：

```java
@Controller
public class UserServlet {
    private UserService userService;

    @Autowired
    @Qualifier("userService1")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

## 五、注解实现创建过程比较复杂的对象的 IoC 与 DI

#### 基本实现

纯 XML 开发方式下，我们是用 Spring 自带的 FactoryBean 法创建了一个 FactoryBean 类，然后在 Spring 配置文件完成了 FactoryBean 对象的创建与依赖注入。

XML + 注解混合开发方式下，我们依然得用 Spring 自带的 FactoryBean 法创建一个 FactoryBean 类，但是不是在 Spring 配置文件里做 FactoryBean 对象的创建与依赖注入，而是直接在 FactoryBean 类里做。

```java
// ConnectionFactoryBean.java

// FactoryBean 类，类似于前面的工厂类
// 必须实现 Spring 自带的 FactoryBean 接口，并且指定我们要创建的 Bean 类型
// 这个类是用来创建 connection 对象的，而不是用来创建它自己的对象的，所以 beanId 直接叫 connection 即可，不能要默认的 connectionFactoryBean
@Component("connection")
public class ConnectionFactoryBean implements FactoryBean<Connection> {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    @Value("com.mysql.cj.jdbc.Driver")
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Value("jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("root")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("mysqlroot")
    public void setPassword(String password) {
        this.password = password;
    }

    // 这个方法类似于前面的静态方法或实例方法
    @Override
    public Connection getObject() throws Exception {
        // 第一步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
        Class.forName(driverClassName);
        // 第二步：利用 DriverManager 创建一个数据库连接对象 connection
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

    // 这个方法固定返回我们要创建的 Bean 类型
    @Override
    public Class<?> getObjectType() {
        return Connection.class;
    }
}
```

#### 补充：@PropertySource 注解

前面的演示里，我们是把数据库相关配置的值直接写死在 Java 类里注入的，但是实际开发中我们一般都是把数据库相关配置的值写在独立的配置文件里，然后在 Java 类里引入独立的配置文件，这样一来我们如果想改一下数据库相关配置的值，直接改独立的配置文件即可， Java 类根本不用动。

@PropertySource 注解就是之前 context:property-placeholder 标签的功能，用来引入其它配置文件，只不过 context:property-placeholder 标签是写在 Spring 配置文件里，@PropertySource 注解是写在我们自己定义的 Java 类里。

* `database.properties`

```properties
# key 用小驼峰，value 不用加 ""
# key-value 的分隔符是 = 或 : ，推荐使用 = ，= 的左右两边不要加空格

driverClassName=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC
username=root
password=mysqlroot
```

* `ConnectionFactoryBean.java`

```java
// FactoryBean 类，类似于前面的工厂类
// 必须实现 Spring 自带的 FactoryBean 接口，并且指定我们要创建的 Bean 类型
// 这个类是用来创建 connection 对象的，而不是用来创建它自己的对象的，所以 beanId 直接叫 connection 即可，不能要默认的 connectionFactoryBean
@Component("connection")
@PropertySource("database.properties")
public class ConnectionFactoryBean implements FactoryBean<Connection> {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    @Value("${driverClassName}")
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Value("${url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${username}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${password}")
    public void setPassword(String password) {
        this.password = password;
    }

    // 这个方法类似于前面的静态方法或实例方法
    @Override
    public Connection getObject() throws Exception {
        // 第一步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
        Class.forName(driverClassName);
        // 第二步：利用 DriverManager 创建一个数据库连接对象 connection
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

    // 这个方法固定返回我们要创建的 Bean 类型
    @Override
    public Class<?> getObjectType() {
        return Connection.class;
    }
}
```

## 六、注解实现 AOP

#### 基本实现

纯 XML 开发方式下，我们需要创建一个实现了 MethodInterceptor 接口的 Interceptor 类，在这个类里写好`附加代码`，然后在 Spring 配置文件配置`切面`、`切入点`、`advisor`。

XML + 注解混合开发方式下，我们要想实现 AOP，要做五步：

* 第一步：在 Spring 配置文件里写个 aop:aspectj-autoproxy 标签

* 第二步：自定义一个类，用 @Aspect 注解标识一下这个类是个`切面类`（@Aspect 注解就是之前 aop:config 标签的功能，用来表示一个切面，现在一个切面类就是一个切面），再用 @Component 注解让 Spring 自动创建这个类的对象并放到 IoC 容器里
* 第三步：然后定义一个空方法，在这个空方法上面用 @Pointcut 注解来配置给哪些类的哪些方法附加代码（@Pointcut 注解就是之前 aop:pointcut 标签的功能，用来表示一个`切入点`）
* 第四步：然后在这个类里定义一个一个的方法（方法名随便、参数必须是 ProceedingJoinPoint、返回值也必须是 Object），这一个一个的方法就是之前一个一个的 Interceptor 类里的 invoke 方法，这一个一个的方法就是业务层代理的方法，我们就是把`附加代码`写在这一个一个的方法里
* 第五步：然后在这一个一个的方法上面用 @Around 注解把切入点和附加代码给整合起来（@Aspect 注解就是之前 aop:advisor 标签的功能，用来把切入点和附加代码给整合起来）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd">
    <!--
        通过 context:component-scan 标签告诉 Spring 框架哪个包里的类是通过注解实现 IoC 的
        Spring 框架就会扫描这个包里所有有注解的类来自动创建对象并放到 IoC 容器里
    -->
    <context:component-scan base-package="com.ineyee._04_aop.basicuse"/>

    <!-- 注解实现 AOP 相关 -->
    <aop:aspectj-autoproxy/>
</beans>
```

```java
@Aspect
@Component
public class DefaultAspect {
    @Pointcut("execution(* com.ineyee._04_aop.basicuse.service..*.*(..))")
    public void pointcut() {
    }

    // 我们可以认为这个方法就是业务层代理的方法
    // 我们只需要专心考虑附加代码怎么写就可以了
    @Around("pointcut()")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        // 业务层代理调用业务层的代码（相当于中介跟房东对接）
        Object result = point.proceed();

        // 业务层的附加代码都抽取到业务层代理中来了
        System.out.println("假设这里是【日志存储】的附加代码");

        return result;
    }
}
```

* 控制器层的代码一点都不用动，加个 @Controller 和 @Autowired 注解即可

```java
@Controller
public class UserServlet {
    // 控制器层表面还是持有业务层，实际却是持有业务层代理（本质原因是我们在 afterInit 生命周期方法里把创建业务层对象“篡改”成了创建业务层代理对象）
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Boolean login(String username, String password) {
        // 控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码（相当于租客跟中介对接）
        return userService.login(username, password);
    }
}
```

* 业务层只写业务代码，加个 @Service 注解即可

```java
public interface UserService {
    Boolean login(String username, String password);
}

@Service("userService")
public class UserServiceImpl implements UserService {
    @Override
    public Boolean login(String username, String password) {
        // 业务层只写业务代码
        System.out.println("假设这里是【业务规则校验】的业务代码，去数据库里查询用户和密码是否匹配");
        System.out.println("假设这里是【调用数据层 API】的业务代码，调用数据层的 login 方法");

        return true;
    }
}
```

