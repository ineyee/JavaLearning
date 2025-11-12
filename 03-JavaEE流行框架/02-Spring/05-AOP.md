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
> 静态代理有三个问题：
>
> * 基本上我们要为每个 XxxService.java 业务层都编写一个代理类，所以会引入大量的 .java 代理类文件
> * 不同的 .java 代理类文件里可能会存在大量重复的代码，比如说我们要为登录业务单独添加日志存储代理，要为下单业务单独添加日志存储代理，日志存储的的代码其实是一样的
> * 虽然我们已经做到了“既然是给业务层做附加代码，那就只修改业务层的代码，控制器层的代码一行都不要动”，但其实我们还修改了 Spring 的配置文件来修改注入的对象，最完美的做法是连配置文件也不改，真得只修改业务层的代码

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

那怎么实现“控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码”呢？

> 核心的一点要求是：确保业务层代理跟业务层拥有一模一样的方法，这样控制器层才能在不改动一行代码的情况下不会报找不到方法的错误。要实现这个要求有两种方案：
>
> * 1、【接口法】业务层实现了哪个接口，就让业务层代理也实现哪个接口，适用的场景：
>   * 当业务层实现了某个接口时（比方说我们的业务层可能有多种方案在切换，用了面向接口编程），这种情况下就可以业务层实现了哪个接口，就让业务层代理也实现哪个接口
> * 2、【继承法】让业务层代理继承业务层或业务层接口的实现类，适用的场景：
>   * 当业务层实现了某个接口时（比方说我们的业务层可能有多种方案在切换，用了面向接口编程），这种情况下也可以让业务层代理直接继承业务层接口的实现类
>   * 当然也有可能业务层只有一种实现方案，我们根本没实现某个接口（没用面向接口编程），这种情况下就只能让业务层代理直接继承业务层
>
> 
>
> 另外一个改动是：Spring 配置文件里依赖注入的地方，不能再给控制器层注入业务层了，而是应该注入业务层代理

```java
// UserServiceProxy.java（接口法）
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

```java
// 继承法
public class UserServiceProxy1 extends UserServiceImpl {
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
<!-- applicationContext_02_staticproxy.xml -->
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
<!--    <bean id="userServiceProxy" class="com.ineyee._02_staticproxy.proxy.UserServiceProxy1">-->
<!--        &lt;!&ndash; 这里给业务层代理注入的是业务层对象 &ndash;&gt;-->
<!--        <property name="userService" ref="userService"/>-->
<!--    </bean>-->

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
> 动态代理可以解决静态代理存在的三个问题：
>
> * 首先我们一个 .java 代理类文件都不创建，只是创建一些跟附加代码相关的 Processor 类，这样就不存在大量的 .java 代理类文件了
> * 其次我们会利用 Processor 类里 Spring Bean 的 AfterInitialization 生命周期方法来统一给业务层附加同样的附加代码，如统一给登录业务和下单业务附加日志存储代码，这样就不存在大量重复的代码了（因为 AfterInitialization 生命周期方法不是针对某一个 bean 对象的，所有 bean 对象都会触发）
> * 然后我们会在 AfterInitialization 生命周期方法里直接返回业务层代理对象，而非原来默认的业务层对象。这样一来就算 Spring 配置文件里写的还是创建“业务层对象”，但实际上 Spring 最终创建出来的是我们“篡改”后的业务层代理对象。于是在不修改 Spring 配置文件的前提下，控制器层获取到的已经是业务层代理对象了。换句话说，动态代理可以真正实现“只修改业务层的代码”，其它任何地方的代码都不动
>
> 但是动态代理也有它自己的不足，Spring AOP 可以解决：
>
> * Processor 类里的代码稍显繁琐，要是能再简单点就好了
>
> * 默认情况下，一份附加代码会附加给所有类的所有方法，如果想精准控制附加给哪个类的哪个方法，我们就需要在 AfterInitialization 生命周期方法里判断类和方法，可以是可以，但这是写在 Java 代码里的，如果线上想改变目标类和目标方法，就不好改了，要是能写在配置文件里就好了

`动态代理和静态代理的核心思路是完全一模一样的，只不过代理类不需要我们手动创建了，是动态生成的而已。`我们依然是通过“引入一个业务层代理”的方式来解决业务层存在的问题，业务层代理专门负责业务层的附加代码，业务层就可以只写业务代码了。这样一来，`之前的项目架构里是控制器层直接调用业务层的代码，而有了业务层代理之后，就是控制器层调用业务层代理的代码，业务层代理再调用业务层的代码。`

因为项目之前可能用的是之前的项目架构，也就是控制器层直接调用业务层的代码，后来可能需要引入业务层代理，所以我们在引入业务层代理时，本着的原则应该还是“既然是给业务层做附加代码，那就只修改业务层的代码，控制器层的代码一行都不要动”，尽管现在改成了“控制器层调用业务层代理的代码”，因此我们就得实现“控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码”。

```java
// UserServlet.java
public class UserServlet {
    // 控制器层表面还是持有业务层，实际却是持有业务层代理（本质原因是我们在 afterInit 生命周期方法里把创建业务层对象“篡改”成了创建业务层代理对象）
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

并且实现“控制器层表面还是在直接调用业务层的代码，实际却是在调用业务层代理的代码”的方案也跟静态代理是一样的。

> 核心的一点要求是：确保业务层代理跟业务层拥有一模一样的方法，这样控制器层才能在不改动一行代码的情况下不会报找不到方法的错误。要实现这个要求有两种方案：
>
> * 1、【接口法，JDK 自带的动态代理方案】业务层实现了哪个接口，就让业务层代理也实现哪个接口，适用的场景：
>   * 当业务层实现了某个接口时（比方说我们的业务层可能有多种方案在切换，用了面向接口编程），这种情况下就可以业务层实现了哪个接口，就让业务层代理也实现哪个接口
> * 2、【继承法，CGLib 三方库的动态代理方案、Spring 框架已经默认集成了这个库】让业务层代理继承业务层或业务层接口的实现类，适用的场景：
>   * 当业务层实现了某个接口时（比方说我们的业务层可能有多种方案在切换，用了面向接口编程），这种情况下也可以让业务层代理直接继承业务层接口的实现类
>   * 当然也有可能业务层只有一种实现方案，我们根本没实现某个接口（没用面向接口编程），这种情况下就只能让业务层代理直接继承业务层
>
> 
>
> ~~另外一个改动是：Spring 配置文件里依赖注入的地方，不能再给控制器层注入业务层了，而是应该注入业务层代理~~

```java
// LogProcessor.java（接口法）
//
// 自定义一个【日志存储】的 Processor 类，继承 BeanPostProcessor，实现其中的两个方法
// 注意：
//     1、这个类和这两个方法不是针对某一个 bean 对象的，所有 bean 对象都会触发，所以我们可以在这个类里写一些所有 bean 对象的公共处理逻辑
//     2、需要在 Spring 配置文件里注册一下这个类才能生效
public class LogProcessor implements BeanPostProcessor {
    // bean 对象的“初始化完成方法”之前会触发
    @Override
    public @Nullable Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 这里的返回值会传递给 postProcessAfterInitialization 方法的参数 bean
        return bean;
    }

    // bean 对象的“初始化完成方法”之后会触发
    @Override
    public @Nullable Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 目前这个日志存储的附加代码，只是给 UserService 业务层附加的，所以得判断一下
        if (bean instanceof UserService || beanName.endsWith("UserService")) {
            // 接口法，JDK 自带的动态代理方案
            //
            // 使用 java.lang.reflect.Proxy 类创建一个业务层代理对象
            // ClassLoader loader：类加载器，类加载器用来将 .class 字节码文件加载到 JVM 里，并解析字节码文件然后生成相应类的类对象
            // Class<?>[] interfaces：业务层实现了哪些接口，通过一个数组的方式传进去，业务层代理对象也得实现这些接口
            // InvocationHandler h：业务层代理的逻辑代码，传一个方法进去，会包含附加代码 + 调用业务层的代码
            //
            // 随便调用哪个类的 getClassLoader 方法都能获取到类加载器
            // 当前 bean 其实就是 userService————我们的业务层对象，我们是要为它动态创建代理对象，所以要把它实现的接口传进去
            // 传一个 lambda 表达式进去，因为业务层代理对象也实现了跟业务层对象一模一样的接口，所以它里面也有 login 等一堆方法，当我们用的业务层代理对象调用 login 等方法时就会触发这个回调
            Object serviceProxyBean = Proxy.newProxyInstance(
                    getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (Object proxy, Method method, Object[] args) -> {
                        // proxy：业务层代理对象
                        // method：业务层的原方法
                        // args：业务层原方法的参数

                        // 业务层代理调用业务层的代码（相当于中介跟房东对接）
                        // bean：当前 bean 其实就是 userService————我们的业务层对象
                        // method：业务层的原方法
                        // args：业务层原方法的参数
                        Object result = method.invoke(bean, args);

                        // 业务层的附加代码都抽取到业务层代理中来了
                        System.out.println("假设这里是【日志存储】的附加代码，当前项目里有一个 login.log 的文件，一旦用户登录成功，就把当前用户是谁、什么时间、什么地点、用什么设备登录的信息持久化到这个文件里以便排查问题");

                        return result;
                    });

            // 这里的返回值已经不是 Spring 框架根据配置文件默认创建出来的业务层对象了，而是经我们“篡改”后的业务层代理对象
            return serviceProxyBean;
        }

        // 这里的返回值就是 Spring 框架根据配置文件默认创建出来的 bean 对象
        return bean;
    }
}
```

```java
// LogProcessor.java（继承法）
//
// 自定义一个【日志存储】的 Processor 类，继承 BeanPostProcessor，实现其中的两个方法
// 注意：
//     1、这个类和这两个方法不是针对某一个 bean 对象的，所有 bean 对象都会触发，所以我们可以在这个类里写一些所有 bean 对象的公共处理逻辑
//     2、需要在 Spring 配置文件里注册一下这个类才能生效
public class LogProcessor1 implements BeanPostProcessor {
    // bean 对象的“初始化完成方法”之前会触发
    @Override
    public @Nullable Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 这里的返回值会传递给 postProcessAfterInitialization 方法的参数 bean
        return bean;
    }

    // bean 对象的“初始化完成方法”之后会触发
    @Override
    public @Nullable Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 目前这个日志存储的附加代码，只是给 UserService 业务层附加的，所以得判断一下
        if (bean instanceof UserService || beanName.endsWith("UserService")) {
            // 继承法，CGLib 三方库的动态代理方案、Spring 框架已经默认集成了这个库
            //
            // 使用 rg.springframework.cglib.proxy.Enhancer 类创建一个 enhancer，然后用 enhancer 创建一个业务层代理对象
            // ClassLoader loader：类加载器，类加载器用来将 .class 字节码文件加载到 JVM 里，并解析字节码文件然后生成相应类的类对象
            // Class superclass：业务层，把业务层这个类传进去，将作为业务层代理类的父类
            // setCallback callback：业务层代理的逻辑代码，传一个方法进去，会包含附加代码 + 调用业务层的代码
            //
            // 随便调用哪个类的 getClassLoader 方法都能获取到类加载器
            // 当前 bean 其实就是 userService————我们的业务层对象，我们是要为它动态创建代理对象，所以要把它的类传进去
            // 传一个 lambda 表达式进去，因为业务层代理类继承了业务层类，所以它里面也有 login 等一堆方法，当我们用的业务层代理对象调用 login 等方法时就会触发这个回调
            Enhancer enhancer = new Enhancer();
            enhancer.setClassLoader(getClass().getClassLoader());
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback((MethodInterceptor) (Object proxy, Method method, Object[] args, MethodProxy methodProxy) -> {
                // proxy：业务层代理对象
                // method：业务层的原方法
                // args：业务层原方法的参数

                // 业务层代理调用业务层的代码（相当于中介跟房东对接）
                // bean：当前 bean 其实就是 userService————我们的业务层对象
                // method：业务层的原方法
                // args：业务层原方法的参数
                Object result = method.invoke(bean, args);

                // 业务层的附加代码都抽取到业务层代理中来了
                System.out.println("假设这里是【日志存储】的附加代码，当前项目里有一个 login.log 的文件，一旦用户登录成功，就把当前用户是谁、什么时间、什么地点、用什么设备登录的信息持久化到这个文件里以便排查问题");

                return result;
            });

            // 这里的返回值已经不是 Spring 框架根据配置文件默认创建出来的业务层对象了，而是经我们“篡改”后的业务层代理对象
            return enhancer.create();
        }

        // 这里的返回值就是 Spring 框架根据配置文件默认创建出来的 bean 对象
        return bean;
    }
}
```

Spring 配置文件一点改动都不需要，保持原来的注入业务层对象即可

```xml
<!-- applicationContext_03_dynamicproxy.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 这个是业务层对象 -->
    <bean id="userService" class="com.ineyee._03_dynamicproxy.service.UserServiceImpl"/>

    <!-- 这个是控制器层对象 -->
    <bean id="userServlet" class="com.ineyee._03_dynamicproxy.servlet.UserServlet">
        <!-- 这里给控制器层注入的是业务层对象 -->
        <property name="userService" ref="userService"/>
    </bean>

    <!-- 注册一下 LogProcessor，使其生效 -->
    <bean class="com.ineyee._03_dynamicproxy.processor.LogProcessor"/>
<!--    <bean class="com.ineyee._03_dynamicproxy.processor.LogProcessor1"/>-->
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

## 四、Spring 是什么

Spring 可以算是 Java 开发中最常用的框架，功能非常强大。Spring 不是位于某一层的框架，而是每一层都跟 Spring 有关系，也就说 Spring 是一个用来整合串联各个层的框架，可以大大简化我们的开发代码。Spring 框架的几个核心概念：

* IoC：Inversion of Control，控制反转，负责创建对象
* DI：Dependency Injection，依赖注入，负责管理对象之间的依赖关系
* `AOP：Aspect Oriented Programming，面向切面编程，Spring 使用 AOP 技术封装了动态代理的功能，让我们使用起动态代理来更加简单。它的底层原理是：如果发现目标类实现了接口，那就用【接口法，JDK 自带的动态代理方案】；如果发现目标类没有实现接口，那就用【继承法，CGLib 三方库的动态代理方案、Spring 框架已经默认集成了这个库】；并且它也是在 AfterInitialization 生命周期方法里创建代理对象的`

## 五、怎么使用 AOP

#### 1、添加依赖

首先安装 Spring：

```XML
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>7.0.0-RC1</version>
</dependency>
```

然后 Spring 的 AOP 依赖了下面两个库

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

然后我们还可以安装一个日志打印库，以便更好地查看 Spring 的执行日志：

```XML
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.19</version>
    <scope>compile</scope>
</dependency>
```

#### 2、编写代码

* 控制器层的代码一点都不用动

```java
// UserServlet.java
public class UserServlet {
    // 控制器层表面还是持有业务层，实际却是持有业务层代理（本质原因是我们在 afterInit 生命周期方法里把创建业务层对象“篡改”成了创建业务层代理对象）
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

* 业务层只写业务代码即可

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

* 创建一个附加代码类，专门用来存放附加代码，至于这些附加代码附加给哪个类的哪个方法，下一步再决定

```java
// LogInterceptor.java
//
// 自定义一个【日志存储】的 Interceptor 类，实现 MethodInterceptor 接口，实现其中的一个方法
// 注意：需要在 Spring 配置文件里注册一下这个类才能生效
public class LogInterceptor implements MethodInterceptor {
    // 我们可以认为这个方法就是业务层代理的方法
    // 里面的代码很简单了，不用像我们自己实现动态代理那样考虑各种乱七八糟的参数是什么意思、怎么传
    // 这里只需要简单一调用，参数都会自动处理
    // 我们只需要专心考虑附加代码怎么写就可以了
    @Override
    public @Nullable Object invoke(MethodInvocation invocation) throws Throwable {
        // 业务层代理调用业务层的代码（相当于中介跟房东对接）
        Object result = invocation.proceed();

        // 业务层的附加代码都抽取到业务层代理中来了
        System.out.println("假设这里是【日志存储】的附加代码");

        return result;
    }
}
```

* 在 Spring 配置文件里新增一些配置，来决定附加代码要附加给哪个类、哪个方法

```xml
<!-- applicationContext_04_aop.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd">
    <!-- 这个是业务层对象 -->
    <bean id="userService" class="com.ineyee._04_aop.service.UserServiceImpl"/>

    <!-- 这个是控制器层对象 -->
    <bean id="userServlet" class="com.ineyee._04_aop.servlet.UserServlet">
        <!-- 这里给控制器层注入的是业务层对象 -->
        <property name="userService" ref="userService"/>
    </bean>

    <!-- 注册一下 LogInterceptor，使其生效，我们的附加代码就在这个里面 -->
    <bean id="logInterceptor" class="com.ineyee._04_aop.aop.LogInterceptor"/>
    <!-- 切面 -->
    <aop:config>
        <!--
            切入点：给哪些类的哪些方法附加代码
                id 属性：切入点的唯一标识
                expression 属性：该属性的常见值为 @annotation(xxx) 和 execution(xxx)
                    @annotation(xxx) 是通过方法注解匹配方法
                    execution(xxx) 是通过方法定义匹配方法

                1、@annotation(xxx)
                    @annotation(xxx) 表示匹配所有带有 @xxx 注解的方法，比如 @annotation(java.lang.Override) 表示匹配所有带有 @Override 注解的方法
                    实际开发中，我们经常会自定义注解来配合 @annotation(xxx) 来使用，写上自定义注解的全名即可
                2、execution(xxx)
                    当值为 execution(* *(..)) 时，表示给所有类的所有方法附加代码
                    第一个 * 的位置，用来匹配方法的返回值类型
                        * 表示任意返回值类型
                        void、int、String 等表示某个具体返回值类型
                    第二个 * 的位置，用来匹配方法名
                        * 表示任意方法名
                        com.ineyee._04_aop.service.*.* 表示这个包里所有类的所有方法（不包含子包里的）
                        com.ineyee._04_aop.service..*.* 表示这个包及其子包里所有类的所有方法
                        com.ineyee._04_aop.service.UserService.* 表示这个类里所有的方法
                        com.ineyee._04_aop.service.UserService.login 表示这个类里的 login 方法
                    (..) 的位置，用来匹配方法的参数列表
                        (..) 表示任意个数、任意类型的参数
                        (T, ..) 表示第一个参数是确定类型 T，后面是任意个数、任意类型的参数
                        (T1, T2) 表示有两个参数，第一个参数是确定类型 T1，第二个参数是确定类型 T2
                        (*) 表示有一个参数，参数类型任意
                        () 表示无参
        -->
<!--        <aop:pointcut id="pointcut" expression="@annotation(java.lang.Override)"/>-->
        <aop:pointcut id="pointcut" expression="execution(* com.ineyee._04_aop.service.UserService.login(..))"/>
        <!-- 通知：按照切入点【pointcut-ref】的配置把附加代码【advice-ref】给附加上去 -->
        <aop:advisor pointcut-ref="pointcut" advice-ref="logInterceptor"/>
    </aop:config>
</beans>
```

