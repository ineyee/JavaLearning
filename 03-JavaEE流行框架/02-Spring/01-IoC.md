## 一、Spring 是什么

Spring 可以算是 Java 开发中最常用的框架，功能非常强大。Spring 不是位于某一层的框架，而是每一层都跟 Spring 有关系，也就说 Spring 是一个用来整合串联各个层的框架，可以大大简化我们的开发代码。Spring 框架的几个核心概念：

* `IoC：Inversion of Control，控制反转，负责创建对象`
* DI：Dependency Injection，依赖注入，负责管理对象之间的依赖关系
* AOP：Aspect Oriented Programming，面向切面编程

## 二、IoC 是什么

之前的代码存在以下问题：

* 比如《02-JavaWeb基础：05-ProjectArchitecture》的代码，Servlet 里需要我们手动创建来持有 service 对象，Service 里需要我们手动创建来持有 dao 对象，但是一旦 Service 层换了实现方案或 Dao 层换了实现方案，我们就只能重新修改 Java 代码来创建另外一个新对象、编译、打包、部署，也就是说代码的耦合性太强了——我依赖你、你被删掉了或被替换了、对我影响很大、我就得改代码
* 诸如此类，很多配置类对象的创建都会存在这样的代码耦合问题（注意：并不是所有的 Java 对象都得用 IoC 来创建，只有那些配置类的 Java 对象才需要（如 dao、service、controller 等），因为上线后在配置文件里方便修改它们的属性；而从数据库里读取出来的 Java 对象就不需要（如 user 等），还是通过 Java 代码手动创建）

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
        bean 标签：通过 Spring IoC 自动创建的对象都叫 bean，所以我们之前的 JavaBean 模型都要改叫 domain 了
            id 属性：当前 bean 对象的唯一标识，用于获取对象
            class 属性：要创建哪个类的对象，全类名
    -->
    <bean id="userServlet" class="com.ineyee.ioc.servlet.UserServlet"/>
    <bean id="userService" class="com.ineyee.ioc.service.UserServiceImpl"/>
    <bean id="userDao" class="com.ineyee.ioc.dao.UserDaoImpl"/>
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

## 补充：scope 属性

当我们通过 IoC 创建对象时，会发现控制台打印了一句“Creating shared instance of singleton bean 'person'”，单例！那这个单例是什么意思呢？

#### 单例 vs 单例设计模式

首先我们知道平常所说的单例设计模式是指“某个类的实例在整个程序运行过程中只有一份”，所以这里的单例绝对不是单例设计模式，因为这里“某个类的实例在整个程序运行过程中可能存在多份”。很简单就能验证，只要我们在整个程序运行过程中创建多个 IoC 容器，然后在不同的 IoC 容器里分别创建同一个类的对象，会发现这些对象并非同一个对象。

* `Person.java`

```java
package com.ineyee.scopeproperty;

public class Person {
}
```

* `applicationContext_scopeproperty.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="person" class="com.ineyee.scopeproperty.Person"/>
</beans>
```

* `App.java`

```java
package com.ineyee.scopeproperty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器
        ApplicationContext applicationContext1 = new ClassPathXmlApplicationContext("applicationContext_scopeproperty.xml");
        Person person1 = (Person) applicationContext1.getBean("person");
        System.out.println(person1); // Person@449a4f23，hashcode 跟 person2 不一样

        // 一个 applicationContext 对应一个 IoC 容器
        ApplicationContext applicationContext2 = new ClassPathXmlApplicationContext("applicationContext_scopeproperty.xml");
        Person person2 = (Person) applicationContext2.getBean("person");
        System.out.println(person2); // Person@173ed316，hashcode 跟 person1 不一样
    }
}
```

#### 单例 vs 同一个 IoC 容器里的单例

有了上面的验证，我们可能会想是不是把作用域从“整个程序运行过程中”缩小为“同一个 IoC 容器里”就行了，也就是说“这里的单例是指某个类的实例在同一个 IoC 容器里只有一份”？事实也并非如此，验证也很简单。

* `Person.java`

```java
package com.ineyee.scopeproperty;

public class Person {
}
```

* `applicationContext_scopeproperty.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="person1" class="com.ineyee.scopeproperty.Person"/>
    <bean id="person2" class="com.ineyee.scopeproperty.Person"/>
</beans>
```

* `App.java`

```java
package com.ineyee.scopeproperty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器，只创建一个 IoC 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_scopeproperty.xml");

        Person person1 = (Person) applicationContext.getBean("person1");
        System.out.println(person1); // Person@5b1669c0，hashcode 跟 person2 不一样

        Person person2 = (Person) applicationContext.getBean("person2");
        System.out.println(person2); // Person@78e4deb0，hashcode 跟 person1 不一样
    }
}
```

#### 单例到底是什么

`这里的单例其实是指“在同一个 IoC 容器里 + 通过同一个 beanId 获取到的对象只有一份”，`也就是说在不同的 IoC 容器里就算通过同一个 beanId 获取到的对象也不是同一份（上面的例子 1），在同一个 IoC 容器里通过不同的 beanId 获取到的对象也不是同一份（上面的例子 2），必须两个条件同时满足才行，验证也很简单。

* `Person.java`

```java
package com.ineyee.scopeproperty;

public class Person {
}
```

* `applicationContext_scopeproperty.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="person" class="com.ineyee.scopeproperty.Person"/>
</beans>
```

* `App.java`

```java
package com.ineyee.scopeproperty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 一个 applicationContext 对应一个 IoC 容器，只创建一个 IoC 容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_scopeproperty.xml");

        Person person1 = (Person) applicationContext.getBean("person");
        System.out.println(person1); // Person@449a4f23，hashcode 跟 person2 一样

        Person person2 = (Person) applicationContext.getBean("person");
        System.out.println(person2); // Person@449a4f23，hashcode 跟 person1 一样
    }
}
```

#### scope 属性

之所以有上面单例的现象，是因为在创建对象时 bean 标签有个 scope 属性，它的默认值是 singleton。

```xml
<bean id="person" class="com.ineyee.scopeproperty.Person" scope="singleton"/>
```

它还有另外一个值是 prototype，只要设置为这个值，那无论什么情况下获取到的该对象都不是同一个对象，即不是单例。

```xml
<bean id="person" class="com.ineyee.scopeproperty.Person" scope="prototype"/>
```

## 补充：Spring Bean 的生命周期

一个 Spring Bean 从出生到死亡，完整的生命周期方法如下，实际开发中我们可以根据需要选择实现：

```java
01 - 构造方法
  如果是通过 setter 方法注入依赖的话，默认是调用无参构造方法
  如果是通过构造方法注入依赖的话，则调用的是我们自定义的构造方法（不常用，下一篇 DI 会详说）
  【01 像在诞生一个新生儿】

02 - 依赖注入的 setter 方法
  如果是通过 setter 方法注入依赖的话，肯定会触发
  如果是通过构造方法注入依赖的话，则不会触发（不常用，下一篇 DI 会详说）
  【02 像在给一个新生儿附加一些社会属性，如姓名、性别、年龄】
  
03 - BeanNameAware 接口的方法
04 - ApplicationContextAware 接口的方法
  BeanNameAware 接口提供了一个方法是 setBeanName，在这个回调方法里我们能获取到创建出来的 bean 对象名字叫什么，需要在类里实现该接口的方法
  ApplicationContextAware 接口提供了一个方法是 setApplicationContext，在这个回调方法里我们能获取到当前 bean 对象所在的 IoC 容器对象是哪个，需要在类里实现该接口的方法
  【03、04 像是给这个新生儿办理的出生证明，我们可以在出生证明上看到这个新生儿的名字、出生的城市】

05 - BeanProcessor 的 BeforeInitialization 方法
  bean 对象的“初始化完成方法”之前会触发
  自定义一个类，继承 BeanPostProcessor，实现其中的两个方法，这个类和这两个方法不是针对某一个 bean 对象的，所有 bean 对象都会触发，所以我们可以在这个类里写一些所有 bean 对象的公共处理逻辑
  
0601 - InitializingBean 接口的方法
  InitializingBean 接口提供了一个方法是 afterPropertiesSet，这个回调方法就是初始化完成的回调方法，需要在类里实现该接口的方法
0602 - init-method 属性对应的方法
  需要在类里自定义好“初始化完成方法”，并在 Spring 配置文件里将 bean 标签的 init-method 属性设置为该方法
  【经历了 01、02、03、04，就代表这个对象彻底初始化好了，框架就会触发初始化完成的回调，0601 和 0602 是两套不同的监听回调方案，我们任选其一即可，做一些加载资源的操作】
  
07 - BeanProcessor 的 AfterInitialization 方法
  bean 对象的“初始化完成方法”之后触发
  自定义一个类，继承 BeanPostProcessor，实现其中的两个方法，这个类和这两个方法不是针对某一个 bean 对象的，所有 bean 对象都会触发，所以我们可以在这个类里写一些所有 bean 对象的公共处理逻辑
  
08 - 我们自定义的各种业务方法
  在对象彻底初始化完成之后，我们会获取到该对象，然后调用该对象的各种业务方法
  
0901 - DisposableBean 接口的方法
  DisposableBean 接口提供了一个方法是 destroy，这个回调方法就是即将销毁的回调方法，需要在类里实现该接口的方法
0902 - destroy-method 属性对应的方法
  需要在类里自定义好“即将销毁方法”，并在 Spring 配置文件里将 bean 标签的 destroy-method 属性设置为该方法
  【0901 和 0902 是两套不同的监听回调方案，我们任选其一即可，做一些释放资源的操作】
```