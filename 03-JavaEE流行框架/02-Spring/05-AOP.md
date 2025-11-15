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

## 六、AOP 的实际应用场景：事务

#### 1、基本使用

最早我们通过 JDBC 实现 dao 层的时候，是通过数据库连接来管理事务的，事务管理写在 dao 层。

```java
// XxxDao.java

// 从连接池里获取一个连接
Connection conn = connectionPool.getConnection();

// JDBC 默认开启了事务，且打开了自动提交
// 所以如果我们想手动管理事务，就得先关闭自动提交，然后在多条 SQL 执行之后手动提交
conn.setAutoCommit(false);

try {
    // 业务...

    // 成功后提交事务
    conn.commit();
} catch (Exception e) {
    // 失败后回滚事务
    conn.rollback();
}
```

但实际开发中，很多业务是跨 dao 的，也就是说一个业务可能要使用多个 dao 才能完成，所以把事务管理写在 dao 层，就只能保证一个 dao 操作的原子性，无法保证一个业务方法执行的原子性，因此实际开发中`事务管理应该总是写在业务层，以此来确保每个业务方法执行的原子性`。所以后来我们通过 MyBatis  实现 dao 层的时候，就把事务管理放到了业务层，MyBatis 是通过 SqlSession 来管理事务的，现在我们是单独使用 MyBatis，此时 SqlSession 既负责执行 SQL 语句也负责管理事务。

```java
// XxxService.java

// 获取 SqlSession
try (SqlSession session = MyBatisUtil.openSession()) {
    // 业务...

    // MyBatis 默认开启了事务，但关闭了自动提交
    // 所以针对“增删改”操作，我们需要成功后手动提交事务，“查”操作不影响数据库，不需要提交事务
    session.commit();
} catch (Exception e) {
    // 失败后回滚事务
    session.rollback();
}
```

再后来我们用 Spring 整合了 MyBatis，现在我们不是单独使用 MyBatis，而是 Spring + MyBatis，此时 SqlSession 只负责执行 SQL 语句，事务管理的工作交给了 Spring，它默认开启了事务，且打开了自动提交，当然 Spring 也给我们提供了手动管理事务的方式，不过需要结合 AOP 技术才能实现（因为事务管理的代码是附加代码，无非就是在业务代码前面附加上开启事务的代码，在业务代码后面附加上提交事务或回滚事务的代码，所以用 AOP 实现再合适不过）。`并且我们也不需要像《五、怎么使用 AOP》里那样在 Java 代码里自定义一个实现了 MethodInterceptor 接口的 Interceptor 类来存放附加代码，因为 Spring 早就给我们提供好了事务管理的附加代码，我们只需要在 Spring 配置文件里配置一下就能完成事务管理了，事务管理写在配置文件里的一大好处是如果线上我们需要修改某些业务的事务管理将会非常方便。`

```java
// XxxService.java
public class MoneyServiceImpl implements MoneyService {
    MoneyDao moneyDao;

    public void setMoneyDao(MoneyDao moneyDao) {
        this.moneyDao = moneyDao;
    }

    @Override
    public void transfer(Integer fromUserId, Integer toUserId, Double money) {
        // 这里会自动切入开启事务的代码
        // fromUserId 扣钱
        moneyDao.update(fromUserId, -money);

        // 模拟业务执行过程中出现异常
        System.out.println(10 / 0);

        // toUserId 加钱
        moneyDao.update(toUserId, money);

        // 成功后，这里会自动切入会提交事务的代码
        // 失败后，这里会自动切入会回滚事务的代码
    }
}
```

```xml
<!-- applicationContext_05_aoptx.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd">
    <!-- 这个是数据层对象 -->
    <bean id="moneyDao" class="com.ineyee._05_aoptx.dao.MoneyDaoImpl"/>

    <!-- 这个是业务层对象 -->
    <bean id="moneyService" class="com.ineyee._05_aoptx.service.MoneyServiceImpl">
        <property name="moneyDao" ref="moneyDao"/>
    </bean>

    <!--
        开发环境和生产环境的数据库连接池及连接及数据库
            开发阶段，我们可以把默认环境设置为开发环境，从而访问测试数据库
            生产阶段，我们可以把默认环境设置为生产环境，从而访问正式数据库

        将会被下面的 txMgr 对象引用
    -->
    <context:property-placeholder location="database-dev.properties"/>
    <bean id="devDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${driverClassName}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
        <property name="initialSize" value="${initialSize}"/>
        <property name="maxActive" value="${maxActive}"/>
    </bean>

    <!--
        Spring 的事务管理器，用来进行事务管理

        将会被下面的 advice 对象引用
    -->
    <bean id="txMgr" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!-- 要进行事务管理，肯定得先拿到跟数据库相关的东西，因为事务管理就是决定如何操作数据库嘛 -->
        <property name="dataSource" ref="devDataSource"/>
    </bean>

    <!--
        这里是附加代码，传个 transaction-manager 进去代表要搞的是事务管理相关的附加代码（这些附加代码 Spring 已经给我们提供好了，我们只需要配置一下即可）

        将会被下面的切面引用
    -->
    <tx:advice id="advice" transaction-manager="txMgr">
        <!-- attributes 标签里需要写明哪些方法需要切入事务管理的附加代码 -->
        <tx:attributes>
            <!--
                一个 method 标签就是一个方法，会结合切入点表达式所设置的类来共同决定到底是哪个类的哪些方法
                    read-only：是否只读
                    propagation：传播行为
                    rollback-for：发生什么异常进行回滚
            -->
            <tx:method name="transfer"/>
            <!-- * 是通配符，代表以 xxx 开头的方法 -->
            <tx:method name="save*"/>
            <tx:method name="remove*"/>
            <tx:method name="update*"/>
            <tx:method name="get*"/>
            <tx:method name="list*"/>
        </tx:attributes>
    </tx:advice>

    <!-- 切面 -->
    <aop:config>
        <!--
            切入点：给哪些类的哪些方法附加代码
            该切入点表示要给 com.ineyee._05_aoptx.service 包及其子包里所有类的所有方法都附加代码

            切入 Spring 事务管理代码的话，这里的切入点表达式只约束到类就可以了，具体的方法由上面的 attributes 指定
        -->
        <aop:pointcut id="pointcut" expression="execution(* com.ineyee._05_aoptx.service..*(..))"/>
        <!-- 通知：按照切入点【pointcut-ref】的配置把附加代码【advice-ref】给附加上去 -->
        <aop:advisor pointcut-ref="pointcut" advice-ref="advice"/>
    </aop:config>
</beans>
```

#### 2、method 标签的其它属性

###### propagation 属性

实际开发中当业务复杂到一定程度时，我们可能会遇见一个业务方法里调用了另外一个业务方法的情况，但是这两个业务方法又都得添加事务管理的代码，此时就会出现事务嵌套，我们就需要处理下事务嵌套的传播行为，否则像下面的例子这样就很容易出现业务方法二的事务已经提交数据库了，但是业务方法一的事务却回滚了，也就是说业务方法一的执行没有保证好原子性，因为该方法内部的一部分数据提交到数据库了，另外一部分数据却因为异常没有提交到数据库，造成了数据库里数据的错乱。

```
// 假设业务方法一里调用了业务方法二

业务方法一开启事务 A
		...

    业务方法二开启事务 B
        ...
    业务方法二执行过程中没有异常，提交事务 B
    
    10 / 0
业务方法一执行过程中出现异常，回滚事务 A
```

propagation 属性就是用来处理这个事的，它可以指定事务嵌套时的传播行为，主要取值如下：

| 取值             | 描述                                                         | 常见使用场景                                                 |
| :--------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| REQUIRED（默认） | 当执行业务方法二时：<br />如果发现业务方法一没有开启事务，那业务方法二就自己开启事务<br />如果发现业务方法一开启了事务，那业务方法二就共用业务方法一的事务，这就可以保证业务方法一的原子性了 | 增、删、改<br />增、删、改一定要通过事务来保证原子性         |
| SUPPORTS         | 当执行业务方法二时：<br />如果发现业务方法一没有开启事务，那业务方法二就不用事务<br />如果发现业务方法一开启了事务，那业务方法二就共用业务方法一的事务 | 查<br />查其实用不用事务都行，所以采取躺平的态度就行         |
| REQUIRES_NEW     | 当执行业务方法二时<br />无论业务方法一有没有开启事务，业务方法二都开启自己的事务，类似于我们上面举的例子 | 日志记录<br />这种情况只适合于业务方法二是做日志记录，无论业务方法一执行过程中有没有抛出异常，业务方法二都得正常提交、不能跟随业务方法一一起提交或回滚 |

###### rollback-for 属性

先回顾一下 Java 里异常的分类：

```
Object
    ————Throwable
		    ————Exception（非检查型异常 + 检查型异常）
		        ————RuntimeException（非检查型异常）
		        ————...（检查型异常）
		    ————Error（非检查型异常）
		        ————...
```

默认情况下，当代码抛出 RuntimeException、Error 这种非检查型异常时才会导致事务回滚，而抛出检查型异常 Exception 时不会导致事务回滚、会正常提交事务。如果我们想让代码抛出检查型异常 Exception 时也能回滚事务，就可以把 rollback-for 属性的值设置为“Exception”，注意把 rollback-for 属性的值设置为“Exception”不是指只有代码抛出 Exception 时才回滚事务，而是指在 RuntimeException、Error 的基础上还有哪些异常抛出时会导致回滚事务。

###### isolation 属性

实际开发中，来自客户端的一个 http 请求会对应一个线程，一个线程里会从数据库连接池里拿到一个数据库连接，然后在这个数据库连接上开启事务、执行 SQL 语句、提交事务。所以当多个客户端同时发起针对同一张表的 http 请求时，就会有多个线程，也就会有多个数据库连接，也就会有多个事务，这就发生了多个事务同一时间操作同一张表的情况。而“多个事务同一时间操作同一张表”，可能引发以下问题：

* 脏读：一个事务读取到了另一个事务没有提交的数据
  * 比如事务一刚刚执行完 INSERT 语句，但是还没来得及提交事务，此时事务二刚好在这个间隙之间执行了 SELECT 语句，那么事务二是能读取到事务一尚未提交的那条数据的，这就是脏读
* 不可重复读：一个事务范围内，针对同一条数据的两次查询却返回了不同数据（跟幻读类似，但不可重复读是针对某一条数据的）
  * 比如事务一一开始就 SELECT 查询了某一条数据，然后就去做耗时操作了，于此同时事务二执行了 UPDATE 语句更新了这条数据并提交了事务，等事务一耗时操作做完后，事务一又需要查询一遍这条数据，查出来却发现跟第一次查询到的数据不一样了，这就是不可重复读
* 幻读：一个事务范围内，针对同一张表的两次查询却返回了不同结果（跟不可重复读类似，但幻读是针对某一张表的）
  * 比如事务一一开始就 SELECT 查询了某一张表的数据总数，然后就去做耗时操作了，于此同时事务二执行了 INSERT 语句新增了一条数据并提交了事务，等事务一耗时操作做完后，事务一又需要查询一遍这张表的数据总数，查出来却发现跟第一次查询到的数值不一样了，这就是幻读

isolation 属性就是用来处理这个事的，它可以设置事务的隔离级别，主要取值如下（由上到下，性能依次降低）：

| 取值             | 特点                                                         |
| ---------------- | ------------------------------------------------------------ |
| READ_UNCOMMITTED | 什么也解决不了                                               |
| READ_COMMITTED   | 可以防止脏读<br />解决办法：事务二没提交的数据，不让事务一能获取到就行了 |
| REPEATABLE_READ  | 可以防止脏读、不可重复读（MySQL 的默认隔离级别，实际开发中我们一般就用默认的隔离级别就行）<br />解决办法：对某一条数据加锁，让事务一两次查询某一条数据、提交事务前，事务二无法操作这条数据 |
| SERIALIZABLE     | 可以防止脏读、不可重复读、幻读<br />解决办法：对整张表加锁，让事务一两次查询某张表、提交事务前，事务二无法操作这张表 |

