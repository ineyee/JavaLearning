## 一、Spring 是什么

Spring 可以算是 Java 开发中最常用的框架，功能非常强大。Spring 不是位于某一层的框架，而是每一层都跟 Spring 有关系，也就说 Spring 是一个用来整合串联各个层的框架，可以大大简化我们的开发代码。Spring 框架的几个核心概念：

* IoC：Inversion of Control，控制反转
* DI：Dependency Injection，依赖注入
* AOP：Aspect Oriented Programming，面向切面编程

## 二、IoC 是什么

之前的代码存在以下问题：

* 比如《02-JavaWeb基础：05-ProjectArchitecture》的代码，Servlet 里需要持有 service 对象，Service 里需要持有 dao 对象，但是一旦 Service 层换了实现方案或 Dao 层换了实现方案，我们就只能重新修改 Java 代码、编译、打包、部署，也就是说代码的耦合性太强了——我依赖你、你被删掉了或被替换了、对我影响很大、我就得改代码
* 诸如此类，对象的创建都会存在这样的问题

而`所谓 IoC 控制反转就是指对象的创建权由我们开发者移交给了 Spring 框架`，之前是我们开发者在代码里手动创建对象，现在则是我们开发者在配置文件里配置好对象、由 Spring 框架自动创建对象，这样一来当“Service 层换了实现方案或 Dao 层换了实现方案”时，我们就只需要改一下配置文件即可，成功解决代码耦合问题

## 三、怎么使用 IoC

#### 1、添加依赖

首先安装 Spring：

```XML
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>7.0.0-RC1</version>
</dependency>
```

然后我们还可以安装一个日志打印库，以便更好地查看 Spring 的执行日志：

```XML
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.19</version>
    <scope>compile</scope>
</dependency>
```

#### 2、创建 MyBatis 的配置文件

在 resources 目录下创建一个名字叫做 mybatis-config.xml 的配置文件，专门用来配置 MyBatis 相关的东西，详见配置文件里。

```XML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--
    configuration 标签里的内容必须按下面的顺序编写：
        1、properties (可选)
        2、settings (可选)
        3、typeAliases (可选)
        4、typeHandlers (可选)
        5、objectFactory (可选)
        6、objectWrapperFactory (可选)
        7、reflectorFactory (可选)
        8、plugins (可选)
        9、environments (可选)
        10、databaseIdProvider (可选)
        11、mappers (可选)
-->
<configuration>
</configuration>
```

#### 3、使用 MyBatis 实现 dao 层

###### 3.1 使用XML

`只定义 dao 接口类，不定义 dao 实现类，用 SqlSession 的 getMapper() 方法自动生成 dao 接口类的实现类的对象。`这里的实现类就类似于我们在《02-JavaWeb基础：05-ProjectArchitecture》里自己定义的 UserDaoImpl 类，这里的实现类对象就类似我们在《02-JavaWeb基础：05-ProjectArchitecture》里自己创建的 UserDao userDao = new UserDaoImpl(); 对象

* 首先创建一个 UserDao 接口类

```Java
package dao;

import bean.UserBean;

import java.util.List;
import java.util.Map;

public interface UserDao {
    UserBean get(Integer id);
    List<UserBean> list(Map<String, Object> params);
    List<UserBean> listPageHelper();
  
    int save(UserBean userBean);
    int saveBatch(List<UserBean> userBeanList);

    int remove(Integer id);
    int removeBatch(List<Integer> idList);

    int update(Map<String, Object> params);
    int updateBatch(Map<String, Object> params);
}
```

* 然后在 resources 目录下创建一个名字叫做 mappers 的文件夹，该目录下的一个映射文件对应一张表和一个 Java Bean，`我们就是在这些映射文件里编写 SQL 语句来操作数据库并自动完成数据表和 Java Bean 的转换，这些映射文件其实就是 dao 层。`在 mappers 目录下创建一个名字叫做 user.xml 的映射文件，顾名思义，这个映射文件专门用来配置数据库里 t_user 表和 Java 代码里 UserBean 的映射关系，详见映射文件里
  * `一定要在 mybatis-config.xml 配置文件里注册一下这个映射文件，否则 MyBatis 无法找到这个映射文件`
  * `一定要确保映射文件里的 namespace 和标签按照强制规范来写，否则 MyBatis 无法完成实现类及其对象跟接口类的匹配`

```XML
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--
    namespace 必须命名为对应 dao 接口类的全类名，因为这个 xml 文件会被匹配为对应 dao 接口类的实现类即数据层。如果名字不匹配的话，MyBatis 无法自动将当前文件做为对应接口类的实现
-->
<mapper namespace="dao.UserDao">
    <!--
				SQL 语句的唯一标识必须和对应 dao 接口类里的方法名一致，参数和返回值的类型也必须保持一致，同样地MyBatis 要完成自动匹配
    -->
    <select id="get" parameterType="Integer" resultType="UserBean">
    </select>
    <select id="list" parameterType="Map" resultType="UserBean">
    </select>
    <select id="listPageHelper" parameterType="Map" resultType="UserBean">
    </select>

    <insert id="save" parameterType="UserBean">
    </insert>
    <insert id="saveBatch" parameterType="List" useGeneratedKeys="true" keyProperty="id">
    </insert>

    <delete id="remove" parameterType="Integer">
    </delete>
    <delete id="removeBatch" parameterType="List">
    </delete>

    <update id="update" parameterType="Map">
    </update>
    <update id="updateBatch" parameterType="Map">
    </update>
</mapper>
```

###### 3.2 使用注解

`只定义 dao 接口类，不定义 dao 实现类，用 SqlSession 的 getMapper() 方法自动生成 dao 接口类的实现类的对象。`这里的实现类就类似于我们在《02-JavaWeb基础：05-ProjectArchitecture》里自己定义的 UserDaoImpl 类，这里的实现类对象就类似我们在《02-JavaWeb基础：05-ProjectArchitecture》里自己创建的 UserDao userDao = new UserDaoImpl(); 对象

创建一个 UserDao 接口类

* `一定要在 mybatis-config.xml 配置文件里注册一下这个接口类，否则 MyBatis 无法找到这个接口类`
* `直接在接口类里用注解编写 SQL 语句，连 xml 文件都不需要了，namespace、SQL 语句的唯一标识、参数和返回值类型也都会自动反射`

```Java
package dao;

import bean.UserBean;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserDaoAnno {
    @Select("""
            SELECT id,
                    name,
                    age,
                    height,
                    email,
                    create_time,
                    update_time
            FROM t_user
            WHERE id = #{id};
            """)
    UserBean get(Integer id);

    @Select("""
            SELECT id,
                   name,
                   age,
                   height,
                   email,
                   create_time,
                   update_time
            FROM t_user
            ORDER BY create_time DESC
                     -- limit、offset 是参数名，因为外界传进来一个 Map，所以参数名只要和 Map 中的 key 一一对应，就能顺利匹配到参数值
                LIMIT #{limit}
            OFFSET #{offset};
            """)
    List<UserBean> list(Map<String, Object> params);

    @Select("""
            SELECT id,
                   name,
                   age,
                   height,
                   email,
                   create_time,
                   update_time
            FROM t_user
            ORDER BY create_time DESC
            """)
    List<UserBean> listPageHelper();

    // 需自动回填字段
    int save(UserBean userBean);

    // 需自动回填字段 + foreach 批量处理
    int saveBatch(List<UserBean> userBeanList);

    @Delete("""
            DELETE
            FROM t_user
            WHERE id = #{id};
            """)
    int remove(Integer id);

    // 需 foreach 批量处理
    int removeBatch(List<Integer> idList);

    // 需 if 动态 SQL
    int update(Map<String, Object> params);

    // 需 if 动态 SQL + foreach 批量处理
    int updateBatch(Map<String, Object> params);
}
```

###### 3.3 使用 XML 和使用注解对比

使用注解

* 优势：不用再写一大堆的 xml 文件及其配置，项目结构更加简单
* 劣势：
  * 注解不太擅长处理需要自动回填字段、if 动态 SQL、foreach 批量处理等复杂场景，虽然它能处理，但是处理起来的阅读性和可维护性反倒不如 xml 来得清晰直观
  * 注解的 SQL 语句是写在 Java 代码里的，也就是说 SQL 和 Java 是耦合的，导致 SQL 语句也会被编译成 class 字节码文件，如果我们想改 SQL 语句，那就只能重新修改 Java 代码、编译、打包、部署

使用 XML

* 优势：
  * xml 的功能非常强大，简单场景、复杂场景都能应对
  * xml 的 SQL 语句是写在 .xml 配置文件里的，也就是说 SQL 和 Java 是解耦的，如果我们想改 SQL 语句，直接找到对应的 .xml 文件修改即可
* 劣势：
  * 需要写一大堆的 xml 文件及其配置

`所以实际开发中，还是以 XML 为主。`如果实在要用注解，那就简单的场景用注解，复杂的场景用 XML，两者混合使用也是可以的，但是注意两者混合使用时只需要注册 xml 就可以了，不需要再注册接口类，否则会报错重复注册。

#### 4、创建单元测试验证 dao 层是否正确

详见 UserTest 文件里。

```Java
@Test
void get() {
    try (SqlSession session = MyBatisUtil.openSession()) {
        UserDao userDao = session.getMapper(UserDao.class);

        UserBean userBean = userDao.get(21);
        if (userBean != null) {
            System.out.println("查询成功：" + userBean);
        } else {
            System.out.println("查询失败");
        }
    }
}
```

#### 5、替换 JDBC 实现的 dao 层

接下来我们就用 MyBatis 实现的 dao 层，替换掉《02-JavaWeb基础：05-ProjectArchitecture》里用 JDBC 实现的 dao 层，详见项目里。