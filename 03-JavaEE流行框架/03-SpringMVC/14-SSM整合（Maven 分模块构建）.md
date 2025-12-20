本篇我们在《13-ssm-pure-annotation》的基础上，用 Maven 分模块构建项目，采取按层拆分的方式

## 一、分模块创建项目

当项目规模比较庞大时，我们可以考虑对项目进行拆分，分模块构建项目。常见的拆分思路：

* 搞一个主项目——即父项目

* 搞一堆子项目

  * 子项目可以按业务模块拆分：员工模块、部门模块、工资模块等，一个模块创建一个项目

  * 子项目也可以按层拆分：dao 层、service 层、controller 层等，一层创建一个项目，controller 层依赖 service 层（controller 层里就可以访问到 service 层里定义的东西）、service 层依赖 dao 层（service 层里就可以访问到 dao 层里定义的东西）

这样一来所有的子项目就都会继承自主项目，继承的主要体现就是子项目会继承主项目 pom.xml 里的一些内容，比如 \<dependencies\>、\<properties\>、\<dependencyManagement\>，但是子项目是继承不到主项目里其它东西的，所以主项目的 src 目录可以删掉了、我们不会在主项目里写任何东西、它就像一个空壳项目一样、我们只是利用它的 pom.xml 文件。因此我们就可以把所有项目都会用到的依赖统一放到主项目里，比如日志打印、单元测试等，我们还可以把所有项目都会设置的属性统一放到主项目里，比如 Maven 编译、打包源码时的编码方式、字节码文件版本等。

#### 1、主项目——即父项目

###### 1.1 创建项目

用 IDEA 创建一个 Maven 项目/模块：

* IDEA - New Project - Maven Archetype
* Name：14-ssm-maven-multi-modules
* Location：项目所在父目录

* JDK：选择相应版本的 JDK
* Catalog：提供不同的 Archetype 列表
  * Internal（一般选这个就够用了）：IDE 自带的 Archetype 列表，不依赖网络，模板有限
  * Default Local：本地 Maven 仓库里已有的 Archetype 列表（~/.m2/repository），如果之前下载过模板会显示
  * Maven Central：官方 Maven 中央仓库提供的 Archetype 列表，网络可访问最新模板，模板最全

* Archetype：Maven 的项目模板，用来快速生成一个规范化的项目结构和配置文件，主项目选择 maven-archetype-quickstart，因为我们将来部署的不是它
  * maven-archetype-webapp：Web 项目模板，生成 WEB-INF、web.xml 等基础结构
  * maven-archetype-quickstart：最基本的 Java 项目模板，包含 main/test 目录

* GroupId（公司域名倒写）：com.ineyee
* Artifact（默认就是项目名）
* Version（项目版本号）：1.0.0
* Create（这样就创建好了主项目——即父项目）

###### 1.2 项目目录结构划分

```
├─${project-name}/(主项目)
│  ├─dao/(dao 子项目)
│  ├─service/(service 子项目)
│  ├─controller/(controller 子项目)
│  ├─pom.xml(主项目的配置文件，里面记录着主项目的很多信息)
```

###### 1.3 Maven 项目配置

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!--
        声明 pom.xml 文件的版本，就像 .html 文件里声明是 H5 那样，决定了该文件里能写什么标签不能写什么标签
        目前都使用 4.0.0，是必要元素
    -->
    <modelVersion>4.0.0</modelVersion>

    <!--
      当前项目的 maven 坐标
        groupId：com.ineyee，公司域名倒写，我们创建项目时填写的
        artifactId：默认就是项目名，我们创建项目时填写的
        version：1.0.0，项目版本号，我们创建项目时填写的
    -->
    <groupId>com.ineyee</groupId>
    <artifactId>14-ssm-maven-multi-modules</artifactId>
    <version>1.0.0</version>

    <!-- 子模块 -->
    <modules>
        <module>dao</module>
        <module>service</module>
        <module>controller</module>
    </modules>

    <!-- 所有的依赖 -->
    <dependencies>
    </dependencies>

    <!-- 属性信息 -->
    <properties>
        <!-- 告诉 Maven 编译、打包源码时使用 UTF-8，避免有些环境（如 Windows 服务器）使用系统默认的 GBK、ISO-8859-1 编码 -->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!-- 告诉 Maven 编译器插件，把源码编译成兼容 JDK8 的字节码文件 -->
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <!--
      打包方式
      只要当前项目是别人的父项目，那打包方式就必须得是 pom
    -->
    <packaging>pom</packaging>
</project>
```

#### 2、dao 项目——即子项目

###### 2.1 创建项目

用 IDEA 创建一个 Maven 项目/模块：

* 选中”14-ssm-maven-multi-modules“父项目，右键 - New - Module
* Name：dao
* Location：项目所在父目录，默认就是父项目的目录

* JDK：选择相应版本的 JDK
* Catalog：提供不同的 Archetype 列表
  * Internal（一般选这个就够用了）：IDE 自带的 Archetype 列表，不依赖网络，模板有限
  * Default Local：本地 Maven 仓库里已有的 Archetype 列表（~/.m2/repository），如果之前下载过模板会显示
  * Maven Central：官方 Maven 中央仓库提供的 Archetype 列表，网络可访问最新模板，模板最全

* Archetype：Maven 的项目模板，用来快速生成一个规范化的项目结构和配置文件，dao 子项目选择 maven-archetype-quickstart，因为我们将来部署的不是它
  * maven-archetype-webapp：Web 项目模板，生成 WEB-INF、web.xml 等基础结构
  * maven-archetype-quickstart：最基本的 Java 项目模板，包含 main/test 目录

* GroupId（公司域名倒写）：com.ineyee
* Artifact（默认就是项目名）
* Version（项目版本号）：1.0.0
* Create（这样就创建好了 dao 项目——即子项目）

###### 2.2 项目目录结构划分

```
├─${project-name}/(项目名)
│  ├─src/(项目的源文件)
│  │  ├─main/
│  │  │  ├─java/(我们编写的 Java 代码都放在这个文件夹里)
│  │  │  │  ├─com.ineyee/(公司唯一标识)
│  │  │  │  │  ├─cfg/(之前的 xml 文件现在都变成了 Java 配置类)
│  │  │  │  │  │  ├─DaoConfig.java(之前 SpringConfig 主配置类里关于数据层的配置都抽取到这个类里来了)
│  │  │  │  │  ├─domain/(表现层之模型层，因为数据库拿到数据就要转成模型，所以得放到这个项目里)
│  │  │  │  │  │  ├─BaseDomain
│  │  │  │  │  │  ├─User extends BaseDomain
│  │  │  │  │  ├─dao/(数据层的接口)
│  │  │  │  │  │  ├─UserDao
│  │  │  ├─resources/(我们编写的配置文件都放在这个文件夹里，如 .properties、.xml 文件)
│  │  │  │  ├─mappers/(数据层的实现)
│  │  │  │  │  ├─user.xml
│  │  │  │  ├─dao.properties(数据层 dao 相关配置的值都写在这个配置文件里)
│  ├─target/(项目的打包产物)
│  ├─pom.xml(项目的配置文件，里面记录着项目的很多信息)
```

###### 2.3 Maven 项目配置

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!--
      声明 pom.xml 文件的版本，就像 .html 文件里声明是 H5 那样，决定了该文件里能写什么标签不能写什么标签
      目前都使用 4.0.0，是必要元素
    -->
    <modelVersion>4.0.0</modelVersion>

    <!-- 父项目的 maven 坐标 -->
    <parent>
        <groupId>com.ineyee</groupId>
        <artifactId>14-ssm-maven-multi-modules</artifactId>
        <version>1.0.0</version>
    </parent>

    <!--
      当前项目的 maven 坐标
        groupId：会继承父项目的
        artifactId：默认就是项目名，我们创建项目时填写的
        version：1.0.0，项目版本号，我们创建项目时填写的
    -->
    <artifactId>dao</artifactId>
    <version>1.0.0</version>

    <!-- 所有的依赖 -->
    <dependencies>
    </dependencies>

    <!--
      打包方式，比如 jar、war 等
      如果不写这个标签，默认是打包成 jar
      如果是个 JavaWeb 项目，那应该打包成 war
    -->
    <packaging>jar</packaging>
</project>
```

#### 3、service 项目——即子项目

###### 3.1 创建项目

用 IDEA 创建一个 Maven 项目/模块：

* 选中”14-ssm-maven-multi-modules“父项目，右键 - New - Module
* Name：service
* Location：项目所在父目录，默认就是父项目的目录

* JDK：选择相应版本的 JDK
* Catalog：提供不同的 Archetype 列表
  * Internal（一般选这个就够用了）：IDE 自带的 Archetype 列表，不依赖网络，模板有限
  * Default Local：本地 Maven 仓库里已有的 Archetype 列表（~/.m2/repository），如果之前下载过模板会显示
  * Maven Central：官方 Maven 中央仓库提供的 Archetype 列表，网络可访问最新模板，模板最全

* Archetype：Maven 的项目模板，用来快速生成一个规范化的项目结构和配置文件，service 子项目选择 maven-archetype-quickstart，因为我们将来部署的不是它
  * maven-archetype-webapp：Web 项目模板，生成 WEB-INF、web.xml 等基础结构
  * maven-archetype-quickstart：最基本的 Java 项目模板，包含 main/test 目录

* GroupId（公司域名倒写）：com.ineyee
* Artifact（默认就是项目名）
* Version（项目版本号）：1.0.0
* Create（这样就创建好了 service 项目——即子项目）

###### 3.2 项目目录结构划分

```
├─${project-name}/(项目名)
│  ├─src/(项目的源文件)
│  │  ├─main/
│  │  │  ├─java/(我们编写的 Java 代码都放在这个文件夹里)
│  │  │  │  ├─com.ineyee/(公司唯一标识)
│  │  │  │  │  ├─cfg/(之前的 xml 文件现在都变成了 Java 配置类)
│  │  │  │  │  │  ├─ServiceConfig.java(之前 SpringConfig 主配置类里关于业务层的配置都抽取到这个类里来了)
│  │  │  │  │  ├─api/(给客户端响应数据和错误，因为业务层要抛出业务异常，所以这个类得定义在这里)
│  │  │  │  │  │  ├─exception/(业务异常)
│  │  │  │  │  │  │  ├─ServiceException.java(业务异常)
│  │  │  │  │  ├─service/(业务层)
│  │  │  │  │  │  ├─UserService
│  │  │  │  │  │  ├─UserServiceImpl implements UserService
│  ├─target/(项目的打包产物)
│  ├─pom.xml(项目的配置文件，里面记录着项目的很多信息)
```

###### 3.3 Maven 项目配置（需要通过 dependency 依赖 dao 项目）

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!--
      声明 pom.xml 文件的版本，就像 .html 文件里声明是 H5 那样，决定了该文件里能写什么标签不能写什么标签
      目前都使用 4.0.0，是必要元素
    -->
    <modelVersion>4.0.0</modelVersion>

    <!-- 父项目的 maven 坐标 -->
    <parent>
        <groupId>com.ineyee</groupId>
        <artifactId>14-ssm-maven-multi-modules</artifactId>
        <version>1.0.0</version>
    </parent>

    <!--
      当前项目的 maven 坐标
        groupId：会继承父项目的
        artifactId：默认就是项目名，我们创建项目时填写的
        version：1.0.0，项目版本号，我们创建项目时填写的
    -->
    <artifactId>service</artifactId>
    <version>1.0.0</version>

    <!-- 所有的依赖 -->
    <dependencies>
        <!-- dao 项目 -->
        <dependency>
            <groupId>com.ineyee</groupId>
            <artifactId>dao</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>

    <!--
      打包方式，比如 jar、war 等
      如果不写这个标签，默认是打包成 jar
      如果是个 JavaWeb 项目，那应该打包成 war
    -->
    <packaging>jar</packaging>
</project>
```

#### 4、controller 项目——即子项目

###### 4.1 创建项目

用 IDEA 创建一个 Maven 项目/模块：

* 选中”14-ssm-maven-multi-modules“父项目，右键 - New - Module
* Name：controller
* Location：项目所在父目录，默认就是父项目的目录

* JDK：选择相应版本的 JDK
* Catalog：提供不同的 Archetype 列表
  * Internal（一般选这个就够用了）：IDE 自带的 Archetype 列表，不依赖网络，模板有限
  * Default Local：本地 Maven 仓库里已有的 Archetype 列表（~/.m2/repository），如果之前下载过模板会显示
  * Maven Central：官方 Maven 中央仓库提供的 Archetype 列表，网络可访问最新模板，模板最全

* Archetype：Maven 的项目模板，用来快速生成一个规范化的项目结构和配置文件，controller 子项目选择 maven-archetype-webapp，因为我们将来部署的是它
  * maven-archetype-webapp：Web 项目模板，生成 WEB-INF、web.xml 等基础结构
  * maven-archetype-quickstart：最基本的 Java 项目模板，包含 main/test 目录

* GroupId（公司域名倒写）：com.ineyee
* Artifact（默认就是项目名）
* Version（项目版本号）：1.0.0
* Create（这样就创建好了 controller 项目——即子项目）

###### 4.2 项目目录结构划分

```
├─${project-name}/(项目名)
│  ├─src/(项目的源文件)
│  │  ├─main/
│  │  │  ├─java/(我们编写的 Java 代码都放在这个文件夹里)
│  │  │  │  ├─com.ineyee/(公司唯一标识)
│  │  │  │  │  ├─cfg/(之前的 xml 文件现在都变成了 Java 配置类)
│  │  │  │  │  │  ├─WebInitializer.java(对应之前的 web.xml 配置文件)
│  │  │  │  │  │  ├─SpringConfig.java(对应之前的 applicationContext.xml 配置文件)
│  │  │  │  │  │  ├─SpringMVCConfig.java(对应之前的 dispatchServlet.xml 配置文件)
│  │  │  │  │  ├─api/(给客户端响应数据和错误)
│  │  │  │  │  │  ├─HttpResult.java(给客户端响应数据和错误的包装类)
│  │  │  │  │  │  ├─error/(错误码和错误信息的枚举常量)
│  │  │  │  │  │  │  ├─Error.java(父接口)
│  │  │  │  │  │  │  ├─CommonError implements Error(通用错误码及错误信息)
│  │  │  │  │  │  │  ├─UserError implements Error(用户模块错误码及错误信息)
│  │  │  │  │  │  ├─exception/(业务异常和全局异常处理)
│  │  │  │  │  │  │  ├─❌ ServiceException.java(业务异常，已经放到 service 项目里了，这里就不用再放了，controller 项目里直接导入使用即可，因为 controller 项目依赖了 service 项目)
│  │  │  │  │  │  │  ├─GlobalExceptionHandler.java(全局异常处理)
│  │  │  │  │  ├─controller/(表现层之控制器层)
│  │  │  │  │  │  ├─UserController
│  │  │  │  │  ├─interceptor/(各种拦截器)
│  │  │  │  │  │  ├─HttpLogInterceptor(HTTP 请求日志拦截器)
│  │  │  │  │  ├─filter/(各种过滤器)
│  │  │  │  │  │  ├─CachedBodyFilter(HTTP 请求体缓存过滤器)
│  ├─target/(项目的打包产物)
│  ├─pom.xml(项目的配置文件，里面记录着项目的很多信息)
```

###### 4.3 Maven 项目配置（需要通过 dependency 依赖 service 项目）

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <!--
      声明 pom.xml 文件的版本，就像 .html 文件里声明是 H5 那样，决定了该文件里能写什么标签不能写什么标签
      目前都使用 4.0.0，是必要元素
    -->
    <modelVersion>4.0.0</modelVersion>

    <!-- 父项目的 maven 坐标 -->
    <parent>
        <groupId>com.ineyee</groupId>
        <artifactId>14-ssm-maven-multi-modules</artifactId>
        <version>1.0.0</version>
    </parent>

    <!--
      当前项目的 maven 坐标
        groupId：会继承父项目的
        artifactId：默认就是项目名，我们创建项目时填写的
        version：1.0.0，项目版本号，我们创建项目时填写的
    -->
    <artifactId>controller</artifactId>
    <version>1.0.0</version>

    <!-- 所有的依赖 -->
    <dependencies>
        <!-- service 项目 -->
        <dependency>
            <groupId>com.ineyee</groupId>
            <artifactId>service</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>

    <!-- 构建信息，比如输出产物的名字、插件配置等 -->
    <build>
        <!-- 输出产物的名字 -->
        <finalName>ssm</finalName>
        <!-- 插件配置 -->
        <plugins>
        </plugins>
    </build>

    <!--
      打包方式，比如 jar、war 等
      如果不写这个标签，默认是打包成 jar
      如果是个 JavaWeb 项目，那应该打包成 war
    -->
    <packaging>war</packaging>
</project>
```

###### 4.4 Tomcat 部署配置

- 在 Current File - Edit Configurations - Add New - Tomcat Server - Local - IDE 会自动检测到我们安装在本机的 Tomcat，大多数配置我们不需要修改，只需要修改部分配置
- Deployment - Deploy at the server startup - Add - Artifacts - 一定要选择 controller:war exploded 或 controller:war - OK
- Server - HTTP port，由默认的 8080 换成自定义的端口比如 9999（本机上可能有其它软件已经在监听 8080 端口了，会导致 Tomcat 无法监听；${TOMCAT_HOME}/conf/server.xml 文件，<Connector> 标签里也能修改端口）
- Deployment - Add - Artifact - ${项目名}:war exploded 或 ${项目名}:war
  - ${项目名}:war exploded 代表不打包成 war 包，直接把项目以展开的文件夹形式部署到 Tomcat 上去，适用于开发阶段。也就是说我们在电脑上开发项目的时候适合用这个，因为开发阶段我们要频繁修改代码重新部署来看效果，这样一来就不用每次都打成压缩包，效率更快，换言之就是热部署效率更高
  - ${项目名}:war 代表把项目打包成 war 包，再部署到 Tomcat 上去，适用于发布阶段。也就是说我们在部署到正式或测试服务器的时候适合用这个，因为发布阶段用压缩包，文件传输速率和文件完整性都更容易得到保证
- `把下面的 Application context 由 “/controller:war exploded”这么长的一串换成自定义的“/ssm”（注意前面的 / 不能少，这个应用上下文就是 Tomcat 用来查找对应的项目的）`
- Apply - OK

## 二、编写主项目

把所有项目都会用到的依赖统一放到主项目里，比如日志打印、单元测试等：

* （可选）日志打印

```xml
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.5.19</version>
</dependency>
```

* （可选）单元测试

```xml
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <version>5.8.2</version>
  <scope>test</scope>
</dependency>
```

## 三、编写 dao 子项目

#### 1、添加依赖

###### MyBatis 相关依赖

* （必选）首先安装 MyBatis

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.19</version>
</dependency>
```

* （必选）然后我们知道 MyBatis 对应的是之前的 JDBC API，所以我们还需要安装数据库驱动、连接池

```xml
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

* （可选）然后我们可以安装分页查询库，以便 MyBatis 能用一份 SQL 语句应对不同的数据库如 MySQL、Oracle、PostgreSQL

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>6.1.1</version>
</dependency>
```

###### Spring 整合 MyBatis 特有的依赖

* （必选）Spring 提供了一个

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-jdbc</artifactId>
  <version>7.0.0-RC1</version>
</dependency>
```

* （必选）MyBatis 提供了一个

```xml
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis-spring</artifactId>
  <version>3.0.5</version>
</dependency>
```

* （必选，一定要跟 SpringMVC 的版本一致）Spring，因为这是个独立的项目，要想用上面两个依赖就必须引入 Spring

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>7.0.1</version>
</dependency>
```

###### 检验请求参数是否必传的库（domain 里需要用到）

```xml
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.0.Final</version>
</dependency>
```

#### 2、表现层之模型层 domain

> * 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> * 一个数据库里可以有多张表，比如 user、product 这两张表
> * 一张表对应一组 dao、service、domain、controller，比如 UserDao、UserService、User、UserController、ProductDao、ProductService、Product、ProductController 这两组

纯粹地存储数据，domain 的字段必须和数据库表里的字段一一对应。

#### 3、数据层 dao

> * 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> * 一个数据库里可以有多张表，比如 user、product 这两张表
> * 一张表对应一组 dao、service、domain、controller，比如 UserDao、UserService、User、UserController、ProductDao、ProductService、Product、ProductController 这两组

###### Java 代码

先定义一个 dao 接口，然后再定义一个 mapper 文件、这个 mapper 文件其实就是 dao 接口的实现。

###### 配置

首先创建一个 dao.properties 文件，把数据层 dao 相关配置的值都写在这个配置文件里。

然后在当前项目里创建一个 DaoConfig 类来专门配置一堆数据层 dao 的相关配置，相当于是 controller 项目里主配置类的一个子配置类、为其减负、还内聚在当前项目中，然后在 ServiceConfig 类里导入一下 DaoConfig 类。

## 四、编写 service 子项目

#### 1、添加依赖

（可选）我们可以安装 Spring AOP 相关的库，以便使用 Spring 的面向切面编程往业务层里切入代码、如事务管理

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

#### 2、api 目录里的东西

api 目录里的东西基本都是固定的，可以直接拷贝一份到项目里，后续再根据实际业务做扩展。

#### 3、业务层 service

> * 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> * 一个数据库里可以有多张表，比如 user、product 这两张表
> * 一张表对应一组 dao、service、domain、controller，比如 UserDao、UserService、User、UserController、ProductDao、ProductService、Product、ProductController 这两组

###### Java 代码

先定义一个 service 接口，然后再定义一个 service 接口的实现类、用 @Service 修饰一下放入父 IoC 容器里、自动注入 dao。

###### 配置

然后在当前项目里创建一个 ServiceConfig 类来专门配置一堆业务层 service 的相关配置，相当于是 controller 项目里主配置类的一个子配置类、为其减负、还内聚在当前项目中，然后在 SpringConfig 主配置类里导入 ServiceConfig 类。

## 五、搞 controller 子项目

#### 1、添加依赖

###### SpringMVC 相关依赖

* （必选，一定要跟 Spring 的版本一致）首先安装 SpringMVC

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>7.0.1</version>
</dependency>
```

* （必选）然后我们知道 SpringMVC 是对 Servlet API 的封装，所以我们还需要安装 jakarta.servlet-api

```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.1.0</version>
    <scope>provided</scope>
</dependency>
```

* （可选）把所有参数都获取到一个请求参数模型里时，添加校验参数是否必传相关的库，jakarta.validation-api 是接口，hibernate-validator 是具体实现

```xml
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.0.Final</version>
</dependency>
```

* （可选）然后我们可以安装文件上传库，以便 SpringMVC 能帮我们非常简单地实现文件上传

```xml
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-fileupload2-jakarta-servlet6</artifactId>
  <version>2.0.0-M4</version>
</dependency>
```

###### Java 对象或 Map 和 Json 字符串互转的库

* （可选）然后我们可以安装 Java 对象或 Map 和 Json 字符串互转的库，以便 SpringMVC 能完成“请求体的 Json 字符串自动转 Java 对象或 Map”、“响应体的 Java 对象或 Map 自动转 Json 字符串”，并且这个库的 3.x 版本往后还会自动把 LocalDateTime 转为 "2025-12-20T19:30:45" 格式返回给客户端

```xml
<dependency>
    <groupId>tools.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>3.0.0</version>
</dependency>
```

#### 2、api 目录里的东西

api 目录里的东西基本都是固定的，可以直接拷贝一份到项目里，后续再根据实际业务做扩展。

#### 3、拦截器

HTTP 请求日志拦截器，可以直接拷贝一份到项目里。

#### 4、过滤器

HTTP 请求体缓存过滤器，可以直接拷贝一份到项目里。

#### 5、控制器层 controller

> * 一般来说一个项目对应一个数据库，比如 hello-project-architecture 这个项目和数据库
> * 一个数据库里可以有多张表，比如 user、product 这两张表
> * 一张表对应一组 dao、service、domain、controller，比如 UserDao、UserService、User、UserController、ProductDao、ProductService、Product、ProductController 这两组

###### Java 代码

定义一个 controller 类、用 @Controller 修饰一下放入子 IoC 容器里、自动注入 service。

###### 配置

去 Spring 子配置文件里配置一堆控制器层 controller 的相关配置

再搞下 Spring 主配置文件和 WebInitializer 就可以部署项目了
