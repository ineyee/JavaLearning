## 一、业务层存在的问题和代理设计模式

#### 1、业务层存在的问题

业务层的主要代码：

* 业务代码（必须）：如业务规则校验、调用数据层 API 等
* 附加代码（可选）：如事务管理（事务管理都是写在业务层）、日志存储（可以把业务层的执行日志持久化在日志文件里以便排查问题）、性能监控、异常处理等

这些代码的特点是业务代码是必须的，没有业务代码就无法完成特定的业务，而附加代码是可选的，没有附加代码业务照样能跑，但是有了附加代码后可能更安全或者能给我们带来一些其它的帮助。

```java
// UserService.java
public interface UserService {
    Boolean login(String username, String password);
}

// UserServiceImpl.java
public class UserServiceImpl implements UserService {
    @Override
    public Boolean login(String username, String password) {
        // 业务层既写业务代码
        System.out.println("假设这里是【业务规则校验】的业务代码，去数据库里查询用户和密码是否匹配");
        System.out.println("假设这里是【调用数据层 API】的业务代码，调用数据层的 login 方法");

        // 业务层还写附加代码
        System.out.println("假设这里是【日志存储】的附加代码，当前项目里有一个 login.log 的文件，一旦用户登录成功，就把当前用户是谁、什么时间、什么地点、用什么设备登录的信息持久化到这个文件里以便排查问题");

        return true;
    }
}
```

```java
// UserServlet.java
public class UserServlet {
    // 控制器层直接持有业务层
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Boolean login(String username, String password) {
        // 控制器层直接调用业务层的代码
        return userService.login(username, password);
    }
}
```

```xml
<!-- applicationContext_01_beforeproxypattern.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 这个是业务层对象 -->
    <bean id="userService" class="com.ineyee._01_beforeproxypattern.service.UserServiceImpl"/>

    <!-- 这个是控制器层对象 -->
    <bean id="userServlet" class="com.ineyee._01_beforeproxypattern.servlet.UserServlet">
        <!-- 这里给控制器层注入的是业务层对象 -->
        <property name="userService" ref="userService"/>
    </bean>
</beans>
```

`于是问题就来了，实际开发中有时我们必须得在业务层添加附加代码，比如事务管理和日志存储，但是这些代码本来不是业务代码却非得写在业务层，会导致业务层臃肿、累赘。换句话说，我们的目标是：既给业务层添加附加代码，但是附加代码又不能写在业务层里。`

#### 2、代理设计模式

现实生活中其实我们也会遇见类似业务层的问题，比如房东与租客，房东类似于业务层，租客类似于控制器层。

房东的主要代码：

* 业务代码（必须）：签合同、收租金
* 附加代码（可选）：发布房源、带看房源

于是问题就来了，租客是直接与房东对接的，这样房东会很累（房东类似于业务层，发布房源、带看房源的附加代码不得不做，但是自己做的话又很累赘）。换句话说，我们的目标是：既给房东发布了房源、带看了房源，但是这两件事又不用房东自己来做。



现实生活中的解决办法就是引入中介，把房东可选的、不愿意做的附加代码托管给中介，中介的主要代码：

* 发布房源、带看房源

房东就可以不写附加代码，只写必须的业务代码了：

* 签合同、收租金

这样一来，租客跟中介对接，中介再跟房东对接，顺利解决问题。其实这就是`代理设计模式——在不修改目标类里目标方法的前提下、给目标方法增加额外的功能`，中介就是代理对象。

## 二、静态代理（Static Proxy）

> 所谓静态代理是指：我们开发人员需要手动创建代理类，我们的项目目录里会存在一个一个的 .java 代理类文件
>
> 静态代理有一个问题是：基本上我们要为每个 XxxService.java 业务层都编写一个代理类，很繁琐

所以我们就可以通过“引入一个业务层代理”的方式来解决业务层存在的问题，业务层代理专门负责业务层的附加代码，业务层就可以只写业务代码了。这样一来，`之前的项目架构里是控制器层直接调用业务层的代码，而有了业务层代理之后，就是控制器层调用业务层代理的代码，业务层代理再调用业务层的代码。`

因为项目之前可能用的是之前的项目架构，也就是控制器层直接调用业务层的代码，后来可能需要引入业务层代理，所以我们在引入业务层代理时，本着的原则应该是“既然是给业务层做附加代码，那就只修改业务层的代码，控制器层的代码一行都不要动”，尽管现在改成了“控制器层调用业务层代理的代码”，因此我们就得实现“控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码”。

```java
// UserServlet.java
public class UserServlet {
    // 控制器层表面还是持有业务层，实际却是持有业务层代理（本质原因是修改了注入的对象）
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Boolean login(String username, String password) {
        // 控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码（相当于租客跟中介对接）
        return userService.login(username, password);
    }
}
```

那怎么实现“控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码”呢？`核心的一点要求是：业务层实现了哪个接口，业务层代理就得实现哪个接口，这样一来业务层代理才能跟业务层拥有一模一样的方法，控制器层在不改动一行代码的情况下才不会报找不到方法的错误。另外一个改动是：依赖注入的地方，不能再给控制器层注入业务层了，而是应该注入业务层代理，又或者我们想换一个业务层代理、去掉业务层代理，直接修改配置文件就可以了，线上修改配置文件毕竟方便。`

```java
// UserServiceProxy.java
public class UserServiceProxy implements UserService {
    // 业务层代理持有业务层
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Boolean login(String username, String password) {
        // 业务层代理调用业务层的代码（相当于中介跟房东对接）
        Boolean result = userService.login(username, password);

        // 业务层的附加代码都抽取到业务层代理中来了
        System.out.println("假设这里是【日志存储】的附加代码，当前项目里有一个 login.log 的文件，一旦用户登录成功，就把当前用户是谁、什么时间、什么地点、用什么设备登录的信息持久化到这个文件里以便排查问题");

        return result;
    }
}
```

```xml
<!-- applicationContext_01_beforeproxypattern.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 这个是业务层对象 -->
    <bean id="userService" class="com.ineyee._02_staticproxy.service.UserServiceImpl"/>

    <!-- 这个是业务层代理对象 -->
    <bean id="userServiceProxy" class="com.ineyee._02_staticproxy.proxy.UserServiceProxy">
        <!-- 这里给业务层代理注入的是业务层对象 -->
        <property name="userService" ref="userService"/>
    </bean>

    <!-- 这个是控制器层对象 -->
    <bean id="userServlet" class="com.ineyee._02_staticproxy.servlet.UserServlet">
        <!-- 这里给控制器层注入的是业务层代理对象 -->
        <property name="userService" ref="userServiceProxy"/>
    </bean>
</beans>
```

业务层就可以只写业务代码了，顺利解决问题。

```java
// UserService.java
public interface UserService {
    Boolean login(String username, String password);
}

// UserServiceImpl.java
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

## 三、动态代理（Dynamic Proxy）

> 所谓动态代理是指：我们开发人员不需要手动创建代理类，我们的项目目录里不会存在一个一个的 .java 代理类文件，而是在程序运行期间动态生成代理类的字码码
>
> 动态代理可以解决静态代理存在的问题

## 一、Spring 是什么

Spring 可以算是 Java 开发中最常用的框架，功能非常强大。Spring 不是位于某一层的框架，而是每一层都跟 Spring 有关系，也就说 Spring 是一个用来整合串联各个层的框架，可以大大简化我们的开发代码。Spring 框架的几个核心概念：

* IoC：Inversion of Control，控制反转，负责创建对象
* DI：Dependency Injection，依赖注入，负责管理对象之间的依赖关系
* `AOP：Aspect Oriented Programming，面向切面编程`

## 二、DI 是什么

之前的代码存在以下问题：当一个对象内部有一个属性指向其它对象时，我们就得重复创建 ApplicationContext 才能获取到该对象所依赖的那个对象，但是每创建一次 ApplicationContext，都会加载一遍 applicationContext.xml 文件并创建一遍文件里所有的 bean 对象，这显然是重复操作，我们应该只在应用启动时创建一次 ApplicationContext

而所谓 DI 依赖注入就是指`管理对象之间的依赖关系`由我们开发者移交给了 Spring 框架，之前是我们开发者在代码里手动创建对象所依赖的东西，现在则是我们开发者在配置文件里配置好对象所依赖的东西、由 Spring 框架自动创建对象所依赖的东西，这样一来就能`“只在应用启动时创建一次 ApplicationContext，避免了重复创建的问题，并且还能顺利创建出 userService、userDao 这种依赖”`

## 三、怎么使用 DI

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

#### 2、创建 Spring 的配置文件并配置好要创建的对象及其依赖

在 resources 目录下创建一个名字叫做 applicationContext.xml 的配置文件（这个文件名就用大驼峰，不要用中划线，因为是遗留下来的传统，很多 IDE 默认识别的就是这个名字），专门用来配置 Spring 相关的东西，我们需要在这里配置好要创建的对象及其依赖

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
    <bean id="userServlet" class="com.ineyee._02_dibasicuse.servlet.UserServlet">
        <!--
            property 标签：代表当前对象内部有一个属性
                name 属性：属性名是什么
                ref 属性：属性值是什么，当属性的数据类型为自定义对象类型时，得使用 ref 属性给属性赋值，值也是一个 bean 对象、可以通过 bean 对象的唯一标识 id 来获取

            注意：
                private UserService userService; 仅仅是个成员变量
                属性 = 成员变量 + setter 方法 + getter 方法
                而这里是属性，所以我们必须给成员变量提供 setter 方法 + getter 方法（由于这里只是给成员变量赋值，所以只提供 setter 方法也行），否则报错
        -->
        <property name="userService" ref="userService"/>
    </bean>

    <bean id="userService" class="com.ineyee._02_dibasicuse.service.UserServiceImpl">
        <property name="userDao" ref="userDao"/>
    </bean>

    <bean id="userDao" class="com.ineyee._02_dibasicuse.dao.UserDaoImpl"/>
</beans>
```

#### 3、接下来直接获取对象使用即可，Spring 框架会自动创建对象及其依赖

```Java
// App.java
package com.ineyee._02_dibasicuse;

import com.ineyee._02_dibasicuse.servlet.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 读取 Spring 的配置文件，只在这里应用启动时创建一次 ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_02_dibasicuse.xml");

        // 通过 bean 对象的 id 获取对象
        UserServlet userServlet = (UserServlet) applicationContext.getBean("userServlet");
        userServlet.remove(11);
    }
}
```

```Java
// UserServlet.java
package com.ineyee._02_dibasicuse.servlet;

import com.ineyee._02_dibasicuse.service.UserService;

public class UserServlet {
    // 持有 userService 但无需我们手动创建，由 Spring 自动创建
    private UserService userService;

    // 给成员变量提供 setter 方法
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void remove(Integer id) {
        System.out.println("===>UserServlet remove id：" + id);
        userService.remove(id);
    }
}
```

```Java
// UserServiceImpl.java
package com.ineyee._02_dibasicuse.service;

import com.ineyee._02_dibasicuse.dao.UserDao;

public class UserServiceImpl implements UserService {
    // 持有 userDao 但无需我们手动创建，由 Spring 自动创建
    private UserDao userDao;

    // 给成员变量提供 setter 方法
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Boolean remove(Integer id) {
        System.out.println("===>UserServiceImpl remove id：" + id);
        return userDao.remove(id) > 0;
    }
}
```

```Java
// UserDaoImpl.java
package com.ineyee._02_dibasicuse.dao;

public class UserDaoImpl implements UserDao {
    @Override
    public int remove(Integer id) {
        System.out.println("===>UserDaoImpl remove id：" + id);
        return 0;
    }
}
```

## 四、注入方式详解

在《01-Java语言基础：03-面向对象》里我们已经知道给对象的成员变量赋值有两种方法，一是先把对象创建出来、然后再通过 setter 方法一个一个给成员变量赋值，二是自定义构造方法、在构造方法里完成成员变量的赋值，而且我们偏向于使用第一种。

#### 1、通过 setter 方法注入

通过 setter 方法注入其实就对应通过 setter 方法给成员变量赋值，所以这就要求我们必须给 domain 的成员变量提供 setter 方法，比较常用

###### 1.1 property 标签法

传统写法，适用于任何情况

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
        bean 标签：写一个 bean 标签就代表要创建一个对象
            id 属性：对象的唯一标识，用于获取对象
            class 属性：对象所属的类，全类名
    -->
    <bean id="person1" class="com.ineyee._03_dibysetter.domain.Person">
        <!--
            property 标签：写一个 property 标签就代表要给对象的一个属性赋值
                name 属性：属性名是什么
                value 属性 | ref 属性、bean 标签 | 集合标签：属性值是什么
                    (1) 当属性的数据类型为基本数据类型、BigDecimal、字符串时，直接使用 value 属性给属性赋值即可，值总是一个字符串、框架内部会自动完成数据类型转换
                    (2) 当属性的数据类型为自定义对象类型时
                        ① 可以使用 ref 属性给属性赋值，因为值也是一个 bean 对象、所以可以通过 bean 对象的唯一标识 id 来获取，此时属性所指向的对象是一个单例对象
                        ② 也可以使用 bean 标签给属性赋值，此时属性所指向的对象是一个局部对象
                    (3) 当属性的数据类型为集合类型时，使用集合标签给属性赋值
                        ① List 对应 list 标签，Map 对应 map 标签，Set 对应 set 标签
                        ② 当集合里的元素为基本数据类型时、BigDecimal、字符串时，集合标签里嵌套 value 标签指定值；当集合里的元素为自定义对象类型时，集合标签里嵌套 ref 标签（引用单例对象）或 bean 标签（引用局部对象）指定值；当集合里的元素为集合类型时，集合标签里继续嵌套集合标签指定值

            注意：
                属性 = 成员变量 + setter 方法 + getter 方法
                而这里是属性，所以我们必须给成员变量提供 setter 方法 + getter 方法（由于这里只是给成员变量赋值，所以只提供 setter 方法也行），否则报错
        -->
        <property name="partyMember" value="false"/>
        <property name="age" value="18"/>
        <property name="height" value="1.88"/>
        <property name="salary" value="666666.66"/>
        <property name="name" value="张三"/>

        <property name="dog" ref="dog"/>

        <property name="friendList">
            <list>
                <ref bean="person2"/>
                <bean class="com.ineyee._03_dibysetter.domain.Person">
                    <property name="name" value="孙七"/>
                </bean>
            </list>
        </property>
        <property name="foodMap">
            <map>
                <entry key="breakfast" value="馒头"/>
                <entry key="lunch" value="米饭"/>
                <entry key="dinner" value="面条"/>
            </map>
        </property>
        <property name="nicknameSet">
            <set>
                <value>三三</value>
                <value>老三</value>
            </set>
        </property>
    </bean>

    <bean id="person2" class="com.ineyee._03_dibysetter.domain.Person">
        <property name="name" value="李四"/>
        <property name="dog" ref="dog"/>
    </bean>

    <bean id="person3" class="com.ineyee._03_dibysetter.domain.Person">
        <property name="name" value="王五"/>
        <property name="dog">
            <!--
                当 bean 标签写在 property 标签里面时，其实类似于局部对象、就算在同一个 IoC 容器里、框架也会创建多个，所以就不用写 id 以供获取了，因为这个局部对象只属于当前对象的属性
                当某个对象并非只需要创建一个时，就可以把它的 bean 标签放在 property 标签里
                所以 person3 和 person4 持有的 dog 对象是两个不同的 dog 对象
            -->
            <bean class="com.ineyee._03_dibysetter.domain.Dog">
                <property name="name" value="泰迪"/>
            </bean>
        </property>
    </bean>

    <bean id="person4" class="com.ineyee._03_dibysetter.domain.Person">
        <property name="name" value="赵六"/>
        <property name="dog">
            <bean class="com.ineyee._03_dibysetter.domain.Dog">
                <property name="name" value="田园犬"/>
            </bean>
        </property>
    </bean>

    <!--
        当 bean 标签写在最外层时，在同一个 IoC 容器里 + 同一个 beanId、框架其实只会创建一个该对象，也就是一个单例对象
        当某个对象只需要创建一个时，就可以把它的 bean 标签放在最外层
        所以 person1 和 person2 持有的 dog 对象都是这同一个单例 dog 对象
    -->
    <bean id="dog" class="com.ineyee._03_dibysetter.domain.Dog">
        <property name="name" value="旺财"/>
    </bean>
</beans>
```

###### 1.2 property 命名空间法

简便写法，但是只适用于：

* 基本数据类型、BigDecimal、字符串，格式为 p:属性名="值"
* 自定义对象类型的全局单例对象引用，格式为 p:属性名-ref="beanId"

不适用于：

* 自定义对象类型的局部对象
* 集合类型

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
    首先得在根标签这里添加一个 property 命名空间
		约定俗称地我们会把这个命名空间取名为 p，是 property 的首字母
-->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 基本数据类型、BigDecimal、字符串和自定义对象类型的全局单例对象引用可以简写 -->
    <bean id="person1" class="com.ineyee._03_dibysetter.domain.Person"
          p:partyMember="false"
          p:age="18"
          p:height="1.88"
          p:salary="666666.66"
          p:name="张三"
          p:dog-ref="dog">

        <!-- 集合不可以简写 -->
        <property name="friendList">
            <list>
                <ref bean="person2"/>
                <bean class="com.ineyee._03_dibysetter.domain.Person">
                    <property name="name" value="孙七"/>
                </bean>
            </list>
        </property>
        <property name="foodMap">
            <map>
                <entry key="breakfast" value="馒头"/>
                <entry key="lunch" value="米饭"/>
                <entry key="dinner" value="面条"/>
            </map>
        </property>
        <property name="nicknameSet">
            <set>
                <value>三三</value>
                <value>老三</value>
            </set>
        </property>
    </bean>

    <bean id="person2" class="com.ineyee._03_dibysetter.domain.Person"
          p:name="李四"
          p:dog-ref="dog">
    </bean>

    <bean id="person3" class="com.ineyee._03_dibysetter.domain.Person"
          p:name="王五">
        <!-- 自定义对象类型的局部对象不可以简写 -->
        <property name="dog">
            <bean class="com.ineyee._03_dibysetter.domain.Dog">
                <property name="name" value="泰迪"/>
            </bean>
        </property>
    </bean>

    <bean id="person4" class="com.ineyee._03_dibysetter.domain.Person"
          p:name="赵六">
        <property name="dog">
            <bean class="com.ineyee._03_dibysetter.domain.Dog">
                <property name="name" value="田园犬"/>
            </bean>
        </property>
    </bean>

    <bean id="dog" class="com.ineyee._03_dibysetter.domain.Dog"
          p:name="旺财">
    </bean>
</beans>
```

#### 2、通过构造方法注入

通过构造方法注入其实就对应通过构造方法给成员变量赋值，所以这就要求我们必须给 domain 提供自定义构造方法（domain 默认只有一个无参构造方法），不太常用

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
        bean 标签：写一个 bean 标签就代表要创建一个对象
            id 属性：对象的唯一标识，用于获取对象
            class 属性：对象所属的类，全类名
    -->

    <bean id="person1" class="com.ineyee._04_dibyconstructor.domain.Person">
        <!-- 当我们不写任何其它东西时，默认是调用无参构造方法 -->
    </bean>

    <bean id="person2" class="com.ineyee._04_dibyconstructor.domain.Person">
        <!--
            constructor-arg 标签：写几个 constructor-arg 标签就代表要调用有几个参数的构造方法
                name 属性：参数名是什么
                value 属性：参数值是什么

            注意：
                当我们不自定义构造方法的时候，编译器会默认给这个类生成一个无参的构造方法
                而这里是通过构造方法注入，所以我们必须给类提供对应的自定义构造方法，否则报错
        -->
        <constructor-arg name="age" value="18"/>
    </bean>

    <bean id="person3" class="com.ineyee._04_dibyconstructor.domain.Person">
        <constructor-arg name="height" value="1.88"/>
    </bean>

    <bean id="person4" class="com.ineyee._04_dibyconstructor.domain.Person">
        <constructor-arg name="age" value="18"/>
        <constructor-arg name="height" value="1.88"/>
    </bean>
</beans>
```

## 补充：自定义属性的类型转换器

前面的 DI 演示里，我们说到“写一个 property 标签就代表要给对象的一个属性赋值，当`属性的数据类型为基本数据类型、BigDecimal、字符串时`，直接使用 value 属性给属性赋值即可，`值总是一个字符串、框架内部会自动完成数据类型转换`”，之所以会把字符串的值自动转换成其它各种数据类型的值，就是因为框架内部已经默认提供了很多的类型转换器。

但是当属性的数据类型比较特殊时——比如是个 LocalDateTime 类型，我们依然得直接使用 value 属性给属性赋值，值依然总是一个字符串，框架内部却不会自动完成数据类型转换，而是报错，因为框架内部没有默认提供字符串到 LocalDateTime 的类型转换器，此时我们就需要自定义属性的类型转换器。

* `Person.java`

```java
package com.ineyee._05_diconverter.domain;

import java.time.LocalDateTime;

public class Person {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Person{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
```

* `StringDateConverter.java`

```java
package com.ineyee._05_diconverter.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 自定义一个类型转换器
// 要求必须实现 Converter 接口，该接口泛型两个类，代表从什么类型转换成什么类型
public class StringDateConverter implements Converter<String, LocalDateTime> {
    // 定义一个属性，让外界传递进来能把什么格式的时间字符串转换成 LocalDateTime，而不是写死在这个类里
    private String format;

    public void setFormat(String format) {
        this.format = format;
    }

    // 然后重写一下 convert 方法完成转换即可
    @Override
    public LocalDateTime convert(String source) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(source, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("无法将字符串 '" + source + "' 解析为 LocalDateTime，格式应为: " + format, e);
        }
    }
}
```



