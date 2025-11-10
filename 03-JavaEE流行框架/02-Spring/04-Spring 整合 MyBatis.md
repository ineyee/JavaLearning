## 一、Spring 整合 MyBatis

先来回顾一下我们之前使用 MyBatis 的核心步骤（详见《03-JavaEE流行框架：01-MyBatis：01-hello-my-batis》）：

* （没变）1、添加 MyBatis 相关的依赖，如 mybatis、数据库驱动、连接池、分页查询库等

* （变了）`2、创建 MyBatis 的配置文件，在里面配置 MyBatis 相关的东西，如驼峰命名自动映射、类型别名、插件、开发环境和生产环境的数据库连接池及连接及数据库、数据库表自动转 Java Bean 的映射文件等`

* （前半部分没变，后半部分变了、合并到了 2 里）3、定义 dao 接口类，不定义 dao 实现类，而是用 XML 或注解的方式来实现 dao 实现类，`需要事先创建一个 MyBatisUtil 类来创建 SqlSessionFactory 对象和 SqlSession 对象，这样后续才能用 SqlSession 的 getMapper() 方法自动生成 dao 接口类的实现类的对象`
* （没变）4、调用 dao 层的 API 来访问数据库即可

而使用 Spring 整合 MyBatis 可以大大简化我们的开发代码，提高我们的开发效率。

## 二、怎么使用 Spring 整合 MyBatis

#### 1、添加依赖

###### 1.1 Spring 相关的依赖

```XML
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>7.0.0-RC1</version>
</dependency>
```

###### 1.2 MyBatis 相关的依赖

第 1 步是没变，无论是单纯使用 MyBatis，还是使用 Spring 整合 MyBatis，都得添加 MyBatis 相关的依赖

首先安装 MyBatis：

```XML
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.19</version>
</dependency>
```

然后我们知道 MyBatis 对应的是之前的 JDBC API，所以我们还需要安装数据库驱动、连接池：

```XML
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

但是因为 MyBatis 内部自带了连接池，所以要想让 MyBatis 使用三方连接池，还得做一些事情：

* 在 java 目录下创建一个名字叫做 common 的文件夹，在 common 目录下创建一个名字叫做 DruidDataSourceFactory 的类

```Java
package common;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

// 必须继承自 PooledDataSourceFactory
public class DruidDataSourceFactory extends PooledDataSourceFactory {
    public DruidDataSourceFactory() {
        // 必须把 DruidDataSource 设置给 dataSource 属性
        this.dataSource = new DruidDataSource();
    }
}
```

* 去 mybatis-config.xml 配置文件（下面会说到）里配置一下连接池

```XML
<environment id="dev">
    <transactionManager type="JDBC"/>
    <dataSource type="common.DruidDataSourceFactory">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC"/>
        <property name="username" value="root"/>
        <property name="password" value="mysqlroot"/>
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10"/>
    </dataSource>
</environment>
```

我们还可以安装一个分页查询库：

* 因为不同的数据库如 MySQL、Oracle、PostgreSQL 等有不同的分页查询语句，这样我们在数据层就得写不同的 SQL 语句，但是有了分页查询库，我们在数据层就可以写一份 SQL 语句来自适应不同的数据库

```XML
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>6.1.1</version>
</dependency>
```

* 然后去 applicationContext.xml 配置文件（下面会说到）里配置一下分页查询库

```XML
<!-- 插件 -->
<property name="plugins">
    <list>
        <!-- 拦截器设置为 PageHelper 的 PageInterceptor -->
        <bean class="com.github.pagehelper.PageInterceptor">
            <property name="properties">
                <props>
                    <!--
                        reasonable 设置为 true，代表使分页查询合理化：
                            当 pageNum <= 0 时，自动返回第一页的数据
                            当 pageNum > totalPage 时，自动返回最后一页的数据
                    -->
                    <prop key="reasonable">true</prop>
                </props>
            </property>
        </bean>
    </list>
</property>
```

* 使用 PageHelper 实现分页查询的话，SQL 语句里就不用再写 LIMIT OFFSET 了，PageHelper 会自动帮我们拼接上去，但是注意 SQL 语句的结尾不能写分号，否则 PageHelper 帮我们自动拼接的部分就不生效了

```XML
<select id="listPageHelper" parameterType="Map" resultType="User">
    SELECT id,
           name,
           age,
           height,
           email,
           create_time,
           update_time
    FROM t_user
    -- 先按 create_time 降序排序，如果 create_time 相同，再按 id 降序排列
    ORDER BY create_time DESC, id DESC
</select>
```

* 然后在 Java 代码分页查询的地方调用一下 PageHelper.startPage(pageNum, pageSize) 方法就可以了

```JAVA
void listPageHelper() {
    Integer pageSize = 2; // 每页显示多少条数据
    Integer pageNum = 1; // 当前页码，从 1 开始
    PageHelper.startPage(pageNum, pageSize);
    List<User> userBeanList = userDao.listPageHelper();
    if (!userBeanList.isEmpty()) {
        System.out.println("查询成功：" + userBeanList);
    } else {
        System.out.println("查询失败");
    }
}
```

###### 1.3 Spring 整合 MyBatis 特有的依赖

```xml
<!-- Spring 提供了一个 -->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-jdbc</artifactId>
  <version>7.0.0-RC1</version>
</dependency>

<!-- MyBatis 提供了一个 -->
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis-spring</artifactId>
  <version>3.0.0</version>
</dependency>
```

###### 1.4 其它依赖

```XML
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

#### 2、创建 Spring 的配置文件，不需要创建 MyBatis 的配置文件

`第 2 步彻底变了，有了 Spring 之后就不需要 MyBatis 的配置文件了，我们只需要维护好 Spring 的配置文件即可，MyBatis 所有的配置都放到 Spring 的配置文件里了`

```xml

```

#### 3、使用 MyBatis 实现 dao 层

第 3 步的前半部分没变，定义 dao 接口类，不定义 dao 实现类，而是用 XML 或注解的方式来实现 dao 实现类，这里用 XML 实现

* 首先创建一个 UserDao 接口类

```Java
package com.ineyee.dao;

import com.ineyee.domain.User;

import java.util.List;

public interface UserDao {
    List<User> listPageHelper();
}
```

* 然后在 resources 目录下创建一个名字叫做 mappers 的文件夹，该目录下的一个映射文件对应一张表和一个 Java Bean，`我们就是在这些映射文件里编写 SQL 语句来操作数据库并自动完成数据表和 Java Bean 的转换，这些映射文件其实就是 dao 层的实现类。`在 mappers 目录下创建一个名字叫做 user.xml 的映射文件，顾名思义，这个映射文件专门用来配置数据库里 t_user 表和 Java 代码里 User 的映射关系，详见映射文件里
  * `一定要在 mybatis-config.xml 配置文件里注册一下这个映射文件，否则 MyBatis 无法找到这个映射文件`
  * `一定要确保映射文件里的 namespace 和标签按照强制规范来写，否则 MyBatis 无法完成实现类及其对象跟接口类的匹配`

```XML
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--
    namespace，当前文件的命名空间，可以理解为当前文件里所有 SQL 语句唯一标识的默认前缀，避免当前文件里 SQL 语句的唯一标识和别的文件里 SQL 语句的唯一标识重复
    必须命名为对应 dao 接口的全类名，因为这个 xml 文件会被做为对应 dao 接口的实现即数据层。如果名字不匹配的话，MyBatis 无法自动将当前文件做为对应接口的实现
-->
<mapper namespace="com.ineyee.dao.UserDao">
    <!-- SQL 语句的唯一标识必须和对应 dao 接口类里的方法名一致，参数和返回值的类型也必须保持一致，同样地MyBatis 要完成自动匹配 -->
    <select id="listPageHelper" parameterType="Map" resultType="User">
        SELECT id,
               name,
               age,
               height,
               email,
               create_time,
               update_time
        FROM t_user
        ORDER BY create_time DESC, id DESC
    </select>
</mapper>
```

`第 3 步的后半部分变了、合并到了 2 里，之前需要事先创建一个 MyBatisUtil 类来创建 SqlSessionFactory 对象，然后通过 SqlSessionFactory 对象创建 SqlSession 对象，进而用 SqlSession 对象的 getMapper() 方法自动生成 dao 接口类的实现类的对象，现在直接在 Spring 的配置文件里配置一下 mapper 扫描器即可，配置完后我们就可以在代码里通过 applicationContext.getBean("dao 的短类名") 来直接获取到 dao 对象了，连 SqlSession 对象的 getMapper() 方法都不用调用了`

```xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <!-- 需要先指定一下 SqlSessionFactory 对象，以便能用它创建 SqlSession 对象 -->
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    <!--
        然后指定一下 dao 包名即可，相当于自动在当前配置文件里添加了很多 <bean id="XxxDao"/> 来创建一个一个的 dao 对象，并且 bean 标签的 id 就是 dao 的短类名（com.ineyee.dao.XxxDao 里的 XxxDao）
        将来我们需要使用哪个 dao 就能通过 dao 的短类名（即 beanId）直接获取相应的到 dao 对象，连 SqlSession 对象的 getMapper() 方法都不用调用了
    -->
    <property name="basePackage" value="com.ineyee.dao"/>
</bean>
```

#### 4、创建单元测试验证 dao 层是否正确

详见 UserTest 文件里。

```Java
public class UserTest {
    // 读取 Spring 的配置文件
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    // 通过 bean 对象的 id 获取对象
    private final UserDao userDao = (UserDao) applicationContext.getBean("userDao");

    // 查询成功一般是给客户端返回 data=[bean]，查询失败一般是给客户端返回 data=[]
    @Test
    void listPageHelper() {
        Integer pageSize = 2; // 每页显示多少条数据
        Integer pageNum = 1; // 当前页码，从 1 开始
        PageHelper.startPage(pageNum, pageSize);
        List<User> userBeanList = userDao.listPageHelper();
        if (!userBeanList.isEmpty()) {
            System.out.println("查询成功：" + userBeanList);
        } else {
            System.out.println("查询失败");
        }
    }
}
```
