前面我们演示的 IoC 与 DI 都是针对创建过程比较简单的对象，所谓“创建过程比较简单”是指如果我们通过代码手动创建这些对象的话，可以通过 new 直接创建出来。

但是实际开发中还有一些对象的创建过程比较复杂，所谓“创建过程比较复杂”是指如果我们通过代码手动创建这些对象的话，不能通过 new 直接创建出来，而是得调用一系列别人写好的方法才能创建出来。比如我们之前在《02-JavaWeb基础：04-Database》里创建数据库连接对象时，通过代码手动创建是下面这样，那这种对象的 IoC 和 DI 该怎么实现呢？`核心思路是：创建一个中间类，中间类提供一个方法，把对象复杂的创建过程放到中间类的方法里，这样一来我们只要调用中间类的方法就能间接创建该对象了。于是该对象的 IoC 和 DI 联合中间类的 IoC 和 DI 就能顺利让 Spring 框架自动创建它们和管理它们的依赖了。`

```java
private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
private static final String URL = "jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC";
private static final String USERNAME = "root";
private static final String PASSWORD = "mysqlroot";

// 第一步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
Class.forName(DRIVER_CLASS_NAME);
// 第二步：利用 DriverManager 创建一个数据库连接对象 connection
Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
```

## 方法一：静态工厂法

> 静态工厂法的好处是不用创建工厂对象，直接调用一下工厂类的静态方法就能拿到对象了，非常简洁
>
> 静态工厂法的坏处是 DRIVER_CLASS_NAME、URL、USERNAME、PASSWORD 这些配置也必须定义成静态的，而 Spring 框架是设计来给对象做 IoC 和 DI 的，不能给静态属性做，所以这些配置无法通过配置文件的方式提供，以后修改起来不方便

* `ConnectionFactory.java`：创建一个工厂类，工厂类提供一个静态方法（这就是为啥叫静态工厂法的原因），把对象复杂的创建过程放到工厂类的静态方法里，这样一来我们只要调用工厂类的静态方法就能间接创建该对象了

```java
package com.ineyee._01_staticfactory;

import java.sql.Connection;
import java.sql.DriverManager;

// 工厂类
public class ConnectionFactory {
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mysqlroot";

    // 静态方法
    public static Connection getConnection() throws Exception {
        // 第一步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
        Class.forName(DRIVER_CLASS_NAME);
        // 第二步：利用 DriverManager 创建一个数据库连接对象 connection
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        return connection;
    }
}
```

* `applicationContext_01_staticfactory.xml`：于是该对象的 IoC 和 DI 联合工厂类的 IoC 和 DI 就能顺利让 Spring 框架自动创建它们和管理它们的依赖了

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
        bean 标签：写一个 bean 标签就代表要创建一个对象，这种方式（写了 factory-method 属性）创建的就是我们最终想要的 connection 对象
            id 属性：connection 对象的唯一标识，用于获取对象
            class 属性 + factory-method 属性：代表调用工厂类 ConnectionFactory 的方法 getConnection() 来创建 connection 对象
    -->
    <bean id="connection" class="com.ineyee._01_staticfactory.ConnectionFactory" factory-method="getConnection"/>
</beans>
```

## 方法二：实例工厂法

> 实例工厂法的好处是 DRIVER_CLASS_NAME、URL、USERNAME、PASSWORD 这些配置可以定义成非静态的，从而通过配置文件的方式注入，以后修改起来方便
>
> 实例工厂法的坏处是得先创建一个工厂对象，然后再调用工厂对象的实例方法才能拿到对象，多了一步

* `ConnectionFactory.java`：创建一个工厂类，工厂类提供一个实例方法（这就是为啥叫实例工厂法的原因），把对象复杂的创建过程放到工厂类的实例方法里，这样一来我们只要调用工厂类的实例方法就能间接创建该对象了

```java
package com.ineyee._02_instancefactory;

import java.sql.Connection;
import java.sql.DriverManager;

// 工厂类
public class ConnectionFactory {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 实例方法
    public Connection getConnection() throws Exception {
        // 第一步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
        Class.forName(driverClassName);
        // 第二步：利用 DriverManager 创建一个数据库连接对象 connection
        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }
}
```

* `applicationContext_02_instancefactory.xml`：于是该对象的 IoC 和 DI 联合工厂对象的 IoC 和 DI 就能顺利让 Spring 框架自动创建它们和管理它们的依赖了

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
        bean 标签：写一个 bean 标签就代表要创建一个对象，这种方式（没写 factory-method 属性）就是我们之前非常熟悉的创建一个 ConnectionFactory 类型的工厂对象
            id 属性：工厂对象的唯一标识，用于获取对象
            class 属性：对象所属的类，全类名
    -->
    <bean id="connectionFactory" class="com.ineyee._02_instancefactory.ConnectionFactory">
        <!--
            property 标签：写一个 property 标签就代表要给对象的一个属性赋值
                name 属性：属性名是什么
                value 属性：属性值是什么
        -->
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC"/>
        <property name="username" value="root"/>
        <property name="password" value="mysqlroot"/>
    </bean>
    <!--
        bean 标签：写一个 bean 标签就代表要创建一个对象，然后再调用工厂对象的实例方法 getConnection() 来创建 connection 对象
            id 属性：connection 对象的唯一标识，用于获取对象
            factory-bean 属性：要创建 connection 对象的话，需要用哪个工厂对象，获取工厂对象的 beanId 即可
            factory-method 属性：要创建 connection 对象的话，需要用工厂对象的哪个方法
    -->
    <bean id="connection" factory-bean="connectionFactory" factory-method="getConnection"/>
</beans>
```

## 方法三：Spring 自带的 FactoryBean 法

> Spring 自带的 FactoryBean 法的好处是写起来既简洁，配置又可以通过配置文件的方式注入

* `ConnectionFactoryBean.java`：创建一个 FactoryBean 类，必须实现 Spring 自带的 FactoryBean 接口，重写一下 getObject() 方法，把对象复杂的创建过程放到 FactoryBean 类的这个方法里，这样一来我们只要调用 FactoryBean 类的 getObject() 方法就能间接创建该对象了

```java
package com.ineyee._03_factorybean;

import org.springframework.beans.factory.FactoryBean;

import java.sql.Connection;
import java.sql.DriverManager;

// FactoryBean 类，类似于前面的工厂类
// 必须实现 Spring 自带的 FactoryBean 接口，并且指定我们要创建的 Bean 类型
public class ConnectionFactoryBean implements FactoryBean<Connection> {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

* `applicationContext_03_factorybean.xml`：于是该对象的 IoC 和 DI 联合 FactoryBean 类的 IoC 和 DI 就能顺利让 Spring 框架自动创建它们和管理它们的依赖了

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
        bean 标签：写一个 bean 标签就代表要创建一个对象
            id 属性：connection 对象的唯一标识，用于获取对象
            class 属性：对象所属的 FactoryBean 类，全类名

        注意：
            因为 ConnectionFactoryBean 实现了 FactoryBean 接口，所以 Spring 会自动调用 FactoryBean 的 getObject() 方法来创建对象
            所以这种写法直接就是返回一个 connection 对象，而非返回一个 connectionFactoryBean 对象
    -->
    <bean id="connection" class="com.ineyee._03_factorybean.ConnectionFactoryBean">
        <!--
            property 标签：写一个 property 标签就代表要给对象的一个属性赋值
                name 属性：属性名是什么
                value 属性：属性值是什么
        -->
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC"/>
        <property name="username" value="root"/>
        <property name="password" value="mysqlroot"/>
    </bean>
</beans>
```

## 补充：applicationContext.xml 引入其它配置文件

前面的演示里，我们是把数据库相关配置的值直接写死在 applicationContext.xml 里注入的，但是实际开发中 applicationContext.xml 里的内容可能会非常多，我们如果想改一下数据库相关配置的值，就得找来找去，有点麻烦。

所以我们一般都是把数据库相关配置的值写在独立的配置文件里，然后在 applicationContext.xml 里引入独立的配置文件，这样一来我们如果想改一下数据库相关配置的值，直接改独立的配置文件即可，applicationContext.xml 根本不用动。

* `database.properties`

```properties
# key 用小驼峰，value 不用加 ""
# key-value 的分隔符是 = 或 : ，推荐使用 = ，= 的左右两边不要加空格

driverClassName=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC
username=root
password=mysqlroot
```

* `applicationContext_04_importconfigfile.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
    添加三个跟 context 命名空间相关的东西，这样才能写 context 标签来引入其它配置文件
        xmlns:context="http://www.springframework.org/schema/context"
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
-->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 引入其它配置文件 -->
    <context:property-placeholder location="classpath:database.properties"/>

    <bean id="connection" class="com.ineyee._04_importconfigfile.ConnectionFactoryBean">
        <!-- 通过 ${key} 的方式就能读取到 value -->
        <property name="driverClassName" value="${driverClassName}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
    </bean>
</beans>
```

