## 一、yml 配置文件

SpringBoot 项目一启动，就会自动去加载 resources 目录下名字叫 application-* 的配置文件，所以我们在任何使用到该配置文件的地方都不需要手动导入 @PropertySource("xxx")，直接使用就可以了。这个配置文件可以是 .properties、.xml，但是 SpringBoot 推荐使用 .yml（YAML 配置文件），所以我们如果要对 SpringBoot 项目做配置，就得在 resources 目录下创建一个 application.yml 配置文件，比如：

```yaml
# application.yml

# 服务器相关配置（SpringBoot 内置的 Web 容器 Tomcat）
server:
  # 监听的端口，默认是 8080
  port: 9999
  # Application Context Path，默认是 /
  servlet:
    context-path: "/springboot"
```

#### 1、yml 配置文件里一般写些什么

> 这里以 domain 层 Person 类为例演示，但实际开发中一般不会对 domain 层的类进行配置，而是对系统类或三方库类

```yaml
# application.yml

# person 相关配置，这个 person 叫做属性前缀
# 换行直接写，一行代表一对属性名和属性值（属性名的风格是中划线，对应 Java 代码里的小驼峰）
person:
  is-party-member: true
  age: 18
  height: 1.88
  salary: 666666.66
  name: "张三"
  # List
  # 换行直接写，一个横杠代表一个元素
  nicknames:
    - "三三"
    - "老三"
    - "三儿"
  # Map
  # 换行直接写，一行代表一对 key-value
  food-map:
    breakfast: "馒头"
    lunch: "米饭"
    dinner: "面条"
```

#### 2、yml 配置文件的拆分

实际开发中，我们一般不会把所有的配置都写到一个配置文件里，而是主配置文件里只写一些公共的配置，把不同模块的配置拆分到不同的子配置文件里，然后在主配置文件里“引入、激活”子配置文件。主配置文件就是 application.yml，子配置文件一般都是命名为 application-${子配置文件名}.yml：

```yaml
# application.yml

spring:
  profiles:
    # 通过子配置文件名来“引入、激活”子配置文件，这里是个数组
    active:
      - prd # 开发环境用 dev，生产环境用 prd
      - person
```

```yaml
# application-dev.yml

# 服务器相关配置（SpringBoot 内置的 Web 容器 Tomcat）
# 假设在开发环境下端口号是 8888，Application Context Path 是 /springboot-dev
server:
  # 监听的端口，默认是 8080
  port: 8888
  # Application Context Path，默认是 /
  servlet:
    context-path: "/springboot-dev"
```

```yaml
# application-prd.yml

# 服务器相关配置（SpringBoot 内置的 Web 容器 Tomcat）
# 假设在生产环境下端口号是 9999，Application Context Path 是 /springboot
server:
  # 监听的端口，默认是 8080
  port: 9999
  # Application Context Path，默认是 /
  servlet:
    context-path: "/springboot"
```

```yaml
# application-person.yml

# person 相关配置，这个 person 叫做属性前缀
# 换行直接写，一行代表一对属性名和属性值（属性名的风格是中划线，对应 Java 代码里的小驼峰）
person:
  is-party-member: true
  age: 18
  height: 1.88
  salary: 666666.66
  name: "张三"
  # List
  # 换行直接写，一个横杠代表一个元素
  nicknames:
    - "三三"
    - "老三"
    - "三儿"
  # Map
  # 换行直接写，一行代表一对 key-value
  food-map:
    breakfast: "馒头"
    lunch: "米饭"
    dinner: "面条"
```

## 二、属性绑定：把 yml 配置文件里属性的值一次性注入到某个对象的属性上去

#### 1、属性绑定是什么

先来回顾一下 Spring 的依赖注入。在纯注解模式下，我们有两种方式来实现依赖注入：

* @Autowired、@Value 注解法
  * 适用场景：这种方式只适用于那些我们自己定义的 Java 类 + 基本数据类型、BigDecimal、字符串、自定义对象类型的属性，因为只有这些 Java 类我们才能在源码里编写注解，并且当我们自定义的类有集合类型的属性也建议这个类所有的属性都用 @Bean 注解的方法手动设置法来注入
  * 实际应用场景举例：controller 层里注入 service、service 层里注入 dao
* @Bean 注解的方法手动设置法
  * 适用场景：系统的类或三方库的类我们只能通过 @Bean 注解的方法手动设置法来搞，因为我们不能在人家的源码里编写注解
  * 实际应用场景举例：dao 层数据源配置属性的注入、dao 层 MyBatis 配置属性的注入

而 SpringBoot 的属性绑定则是基于 Spring 的依赖注入机制，跟“@Bean 注解的方法手动设置法”这一场景同样的实际用途，但是使用起来更加简单快捷，专门用来实现从 SpringBoot 的配置文件里读取属性的值注入到系统类或三方库类的属性上去。

#### 2、怎么使用属性绑定

> 这里以 domain 层 Person 类为例演示，但实际开发中一般不会对 domain 层的类进行属性绑定，而是对系统类或三方库类

```yaml
# application-person.yml

# person 相关配置，这个 person 叫做属性前缀
# 换行直接写，一行代表一对属性名和属性值（属性名的风格是中划线，对应 Java 代码里的小驼峰）
person:
  is-party-member: true
  age: 18
  height: 1.88
  salary: 666666.66
  name: "张三"
  # List
  # 换行直接写，一个横杠代表一个元素
  nicknames:
    - "三三"
    - "老三"
    - "三儿"
  # Map
  # 换行直接写，一行代表一对 key-value
  food-map:
    breakfast: "馒头"
    lunch: "米饭"
    dinner: "面条"
```

```java
// PersonController.java

@RestController
//@EnableConfigurationProperties(Person.class)
public class PersonController {
    @Autowired
    private Person person;

    @GetMapping("/getPerson")
    public String getPerson() {
        return person.toString();
    }
}
```

先来回顾一下依赖注入怎么实现：

* 创建一个 Person 类，定义成员变量，为所有的成员变量实现 setter、getter 方法，因为属性 = 成员变量 +  setter、getter 方法，依赖注入是给属性注入
* 实现 toString 方法，以便打印观察对象
* 用 @Component 修饰一下 Person 类，以便 Spring 能自动创建对象并放入 IoC 容器中，因为 PersonController 里自动注入 person 对象时的 @Autowired 注解要求对象必须是在 IoC 容器中
* ~~导入配置文件，~~这里就不用导入配置文件了，因为 SpringBoot 项目一启动就会自动去加载配置文件，直接使用即可
* 在各属性的 setter 方法上，用 @Value("${xxx}") 注入配置文件中的值即可（配置文件里的属性名必须和类里的属性名保持一致）

```java
@Component
public class Person {
    private Boolean isPartyMember;
    private Integer age;
    private Double height;
    private BigDecimal salary;
    private String name;

    public String getName() {
        return name;
    }

    @Value("${person.name}")
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    @Value("${person.salary}")
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Double getHeight() {
        return height;
    }

    @Value("${person.height}")
    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getAge() {
        return age;
    }

    @Value("${person.age}")
    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getPartyMember() {
        return isPartyMember;
    }

    @Value("${person.is-party-member}")
    public void setPartyMember(Boolean partyMember) {
        isPartyMember = partyMember;
    }

    @Override
    public String toString() {
        return "Person{" +
                "isPartyMember=" + isPartyMember +
                ", age=" + age +
                ", height=" + height +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                '}';
    }
}
```

属性绑定怎么实现：

> 依赖注入有三个很明显的缺陷：当 Person 类的成员变量不断变多时，类内部的 setter、getter 方法也将不断变多，代码看起来很恶心；我们得用 @Value("${xxx}") 给每个属性一个一个挨个都注入值，很麻烦；@Value 对集合类型的处理不太好弄。属性绑定就能解决这三个缺陷

* 先安装两个依赖

  ```xml
  <!-- 在编译期间帮助生成 setter、getter、toString 等代码 -->
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
  </dependency>
  <!-- 在 application-*.yml 配置文件里编写属性名时能提示 -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
  </dependency>
  ```

* 创建一个 Person 类，定义成员变量，用 @Data 修饰一下 Person 类，这个注解会在编译时自动为所有的成员变量实现 setter、getter 方法，并自动实现 toString 方法
* 用 @Component 修饰一下 Person 类，以便 Spring 能自动创建对象并放入 IoC 容器中，因为 PersonController 里自动注入 person 对象时的 @Autowired 注解要求对象必须是在 IoC 容器中
  * 这里还有另外一种实现方法，如果我们不用 @Component 修饰 Person 类的话，也可以在使用 Person 的地方——比如这里就是 PersonController 类里，用 @EnableConfigurationProperties(Person.class) 修饰一下使用 Person 类的类
* ~~导入配置文件，~~这里就不用导入配置文件了，因为 SpringBoot 项目一启动就会自动去加载配置文件，直接使用即可
* 用 @ConfigurationProperties(prefix = "xxx") 修饰一下 Person 类，xxx 就是配置文件里的属性前缀，代表将哪部分属性的值一次性注入到 Person 对象的属性上去（配置文件里的属性名必须和类里的属性名保持一致）

```java
@Data
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private Boolean isPartyMember;
    private Integer age;
    private Double height;
    private BigDecimal salary;
    private String name;
    private List<String> nicknames;
    private Map<String, Object> foodMap;
}
```