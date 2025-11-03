## 一、Spring 是什么

Spring 可以算是 Java 开发中最常用的框架，功能非常强大。Spring 不是位于某一层的框架，而是每一层都跟 Spring 有关系，也就说 Spring 是一个用来整合串联各个层的框架，可以大大简化我们的开发代码。Spring 框架的几个核心概念：

* IoC：Inversion of Control，控制反转
* DI：Dependency Injection，依赖注入
* AOP：Aspect Oriented Programming，面向切面编程

## 二、IoC 是什么

之前的代码存在以下问题：

* 比如《02-JavaWeb基础：05-ProjectArchitecture》的代码，Servlet 里需要我们手动创建来持有 service 对象，Service 里需要我们手动创建来持有 dao 对象，但是一旦 Service 层换了实现方案或 Dao 层换了实现方案，我们就只能重新修改 Java 代码来创建另外一个新对象、编译、打包、部署，也就是说代码的耦合性太强了——我依赖你、你被删掉了或被替换了、对我影响很大、我就得改代码
* 诸如此类，对象的创建都会存在这样的代码耦合问题

而所谓 IoC 控制反转就是指`对象的创建权`由我们开发者移交给了 Spring 框架，之前是我们开发者在代码里手动创建对象，现在则是我们开发者在配置文件里配置好对象、由 Spring 框架自动创建对象，这样一来当“Service 层换了实现方案或 Dao 层换了实现方案”时，我们就`只需要改一下配置文件`即可，成功解决代码耦合问题

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

#### 2、创建 Spring 的配置文件并配置好要创建的对象

在 resources 目录下创建一个名字叫做 applicationContext.xml 的配置文件（这个文件名就用大驼峰，不要用中划线，因为是遗留下来的传统，很多 IDE 默认识别的就是这个名字），专门用来配置 Spring 相关的东西，我们需要在这里配置好要创建的对象

```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
        通过 Spring IoC 自动创建的对象都叫 bean，所以我们之前的 JavaBean 模型都改叫 domain 了
            class：要创建哪个类的对象，全类名
            id：当前 bean 对象的唯一标识，用于获取对象
    -->
    <bean class="com.ineyee.ioc.servlet.UserServlet" id="userServlet"/>
    <bean class="com.ineyee.ioc.service.UserServiceImpl" id="userService"/>
    <bean class="com.ineyee.ioc.dao.UserDaoImpl" id="userDao"/>
</beans>
```

#### 3、接下来直接获取对象使用即可，Spring 框架会自动创建

`下面“重复创建 ApplicationContext”其实存在问题，那就是每创建一次 ApplicationContext，都会加载一遍 applicationContext.xml 文件并创建一遍文件里所有的 bean 对象，这显然是重复操作，我们应该只在应用启动时创建一次 ApplicationContext。你可能会问“如果只在应用启动时创建一次 ApplicationContext 的话，那 userService、userDao 这种依赖该怎么创建呢”，这就得用到下一篇的依赖注入了 `

```Java
// App.java
package com.ineyee.ioc;

import com.ineyee.ioc.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 读取 Spring 的配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        // 通过 bean 对象的 id 获取对象
        UserServlet userServlet = (UserServlet) applicationContext.getBean("userServlet");
        userServlet.remove(11);
    }
}
```

```Java
// UserServlet.java
package com.ineyee.ioc.servlet;

import com.ineyee.ioc.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServlet {
    // 持有 userService 但无需我们手动创建，由 Spring 自动创建
    private UserService userService;

    public void remove(Integer id) {
        // 读取 Spring 的配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 通过 bean 对象的 id 获取对象
        userService = (UserService) applicationContext.getBean("userService");

        System.out.println("===>UserServlet remove id：" + id);
        userService.remove(id);
    }
}
```

```Java
// UserServiceImpl.java
package com.ineyee.ioc.service;

import com.ineyee.ioc.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceImpl implements UserService {
    // 持有 userDao 但无需我们手动创建，由 Spring 自动创建
    private UserDao userDao;

    @Override
    public Boolean remove(Integer id) {
        // 读取 Spring 的配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 通过 bean 对象的 id 获取对象
        userDao = (UserDao) applicationContext.getBean("userDao");

        System.out.println("===>UserServiceImpl remove id：" + id);
        return userDao.remove(id) > 0;
    }
}
```

```Java
// UserDaoImpl.java
package com.ineyee.ioc.dao;

public class UserDaoImpl implements UserDao {
    @Override
    public int remove(Integer id) {
        System.out.println("===>UserDaoImpl remove id：" + id);
        return 0;
    }
}
```