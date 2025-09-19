## 一、Maven 是什么

Maven 是一个**创建项目工具 + 包管理工具 + 项目构建工具**，最初由 Apache 开发，最常用于 Java 项目。

## 二、本机安装 Maven

Maven 依赖于 JDK，而《Java语言基础》那里我们已经安装好了 JDK，这里直接我们安装一下 Maven：

- Maven 下载地址：https://maven.apache.org/download.cgi
- 这里选择下载 Maven 3.9.11
- 下载完双击解压，把解压后的文件夹拖动到你想要的目录下就算安装完成了，这里选择安装在跟 JDK 一样的目录 /Library/Java/apache-maven-3.9.11，Maven 默认的本地仓库路径为 \~/.m2/repository（/ 代表根目录 Macintosh HD，\~/ 代表当前用户目录 /Users/ineyee）

* 在 .bash_profile 里配置一下环境变量：export PATH="/Library/Java/apache-maven-3.9.11/bin:$PATH"，并执行 source ~/.bash_profile 来让修改立即生效
* 终端执行 mvn --version 或 mvn -v 来验证是否安装成功
* 然后把我们自己安装的 Maven 集成到 IDEA 里，不要用 IDEA 自带的 Maven：
  * IDEA - Settings - Build, Execution, Deployment - Build Tools - Maven
  * Maven home path 换成我们自己安装 Maven 的路径
  * Apply - OK

## 三、本机 Maven 作为创建项目工具

我们知道用 Eclipse 创建的 Java 项目是无法用 IDEA 运行的，用 IDEA 创建的 Java 项目是无法用 Eclipse 运行的，主要是因为用不同的 IDE 创建出来的项目结构和配置文件互不兼容。但是如果用 Maven 来创建的项目的话，它有自己的一套项目结构和配置文件，可以保证不同开发人员使用不同 IDE 时都能够顺利运行项目，下面是用 Maven 创建项目时的基本目录：

```
├─${project-name}/(项目名)
│  ├─src/(项目的源文件)
│  │  ├─main/
│  │  │  ├─java/(我们编写的 Java 代码都放在这个文件夹里)
│  │  │  ├─resources/(我们编写的配置文件都放在这个文件夹里，如 .properties、.xml 文件)
│  │  │  ├─webapp/(JavaWeb 项目的 web 资源)
│  │  ├─test/
│  │  │  ├─java 或 webapp/(单元测试的 Java 代码都放在这个文件夹里)
│  │  │  ├─resources/(单元测试的配置文件都放在这个文件夹里)
│  │  │  ├─webapp/(单元测试的 web 资源)
│  ├─target/(项目的打包产物)
│  ├─pom.xml(项目的配置文件，里面记录着项目的很多信息)
```

用 IDEA 创建一个 Maven 项目/模块：

* IDEA - New Project - Maven Archetype
* Name：hello-tomcat
* Location：项目所在父目录

* JDK：选择相应版本的 JDK
* Catalog：提供不同的 Archetype 列表
  * Internal（一般选这个就够用了）：IDE 自带的 Archetype 列表，不依赖网络，模板有限
  * Default Local：本地 Maven 仓库里已有的 Archetype 列表（~/.m2/repository），如果之前下载过模板会显示
  * Maven Central：官方 Maven 中央仓库提供的 Archetype 列表，网络可访问最新模板，模板最全

* Archetype：Maven 的项目模板，用来快速生成一个规范化的项目结构和配置文件
  * maven-archetype-webapp：Web 项目模板，生成 WEB-INF、web.xml 等基础结构
  * maven-archetype-quickstart：最基本的 Java 项目模板，包含 main/test 目录

* GroupId（公司域名倒写）：com.ineyee
* Artifact（默认就是项目名）
* Version（项目版本号）：1.0.0
* Create（这样就创建好了项目）

## 四、本机 Maven 作为包管理工具

#### 1、pom.xml 文件

pom.xml 文件是项目的配置文件，里面记录着项目的很多信息。

```XML
<!-- 根元素 project -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <!--
		声明 pom.xml 文件的版本，就像 .html 文件里声明是 H5 那样，决定了该文件里能写什么标签不能写什么标签
		目前都使用 4.0.0，是必要元素
	-->
  <modelVersion>4.0.0</modelVersion>
  
  <!--
		groupId：com.ineyee，公司域名倒写，我们创建项目时填写的
		artifactId：默认就是项目名，我们创建项目时填写的
		version：1.0.0，项目版本号，我们创建项目时填写的
	-->
  <groupId>com.ineyee</groupId>
  <artifactId>hello-maven</artifactId>
  <version>1.0.0</version>
  
  <!-- 所有的依赖 -->
  <dependencies>
    <!-- 某一个依赖 -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <!-- 某一个依赖 -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.13.2</version>
    </dependency>
    <!-- 某一个依赖 -->
    <dependency>
      <groupId>jakarta.servlet</groupId>
      <artifactId>jakarta.servlet-api</artifactId>
      <version>6.1.0</version>
      <scope>provided</scope>
    </dependency>
  </dependencies>
  
  <!-- 属性信息 -->
  <properties>
    <!-- 告诉 Maven 编译、打包源码时使用 UTF-8，避免有些环境（如 Windows 服务器）使用系统默认的 GBK、ISO-8859-1 编码 -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <!-- 告诉 Maven 编译器插件，把源码编译成兼容 JDK8 的字节码文件 -->
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
  </properties>
  
  <!-- 构建信息，比如输出产物的名字、插件配置等 -->
  <build>
    <!-- 输出产物的名字 -->
    <finalName>helloMaven</finalName>
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

#### 2、Maven 坐标

groupId、artifactId、version 这三个东西组合在一起称为一个 Maven 坐标，因为它们仨能唯一确定一个项目。

#### 3、依赖

直接在 pom.xml 文件里的 dependencies 标签下添加相应 Maven 坐标的依赖即可，比如：

```XML
<!-- 所有的依赖 -->
<dependencies>
  <!-- 某一个依赖 -->
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>3.8.1</version>
    <scope>test</scope>
  </dependency>
  <!-- 某一个依赖 -->
  <dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.13.2</version>
  </dependency>
  <!-- 某一个依赖 -->
  <dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.1.0</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```

然后点击 Sync Maven Changes，Maven 就会自动去 Maven 远程仓库（中央仓库、公司私有仓库）下载依赖，并缓存到本地仓库（默认 ~/.m2/repository），我们在项目的 External Libraries 里面就能看到新增的依赖了，这样一来，开发阶段所有的项目就都可以共用本地仓库里的三方库了，只有在打包项目的时候才会把需要的三方库从本地仓库复制一份出来到当前项目的打包产物里，从而大大节省我们电脑的磁盘空间。并且 Maven 还会自动下载依赖的依赖、处理依赖冲突等。

## 五、本机 Maven 作为项目构建工具

#### 1、构建生命周期（Build Lifecycle）

`构建生命周期`，描述了使用 Maven 构建项目的整个过程。Maven 内置了 3 个构建生命周期：

* `default（默认，重点讲解）`

* `clean（清理，清理掉 target 里的所有产物，如单元测试代码的编译产物、主代码的打包产物等）`
* site（站点）

而每个构建生命周期内部又由`阶段（Phase）`组成，阶段从上往下按顺序依次执行。比如 default 构建生命周期内部包含以下关键阶段，当我们执行 mvn package 命令时会从上往下按顺序依次执行 validate、compile、test、package 4 个阶段：

* validate 验证：验证项目是否正确，比如验证 pom.xml 文件里的东西是否正确
* compile 编译：对项目的主代码和单元测试代码进行编译
* test 测试：对编译后的单元测试代码进行运行，看看单元测试案例是否全部通过
* `package 打包`：对编译后的主代码进行打包，输出为可分发的产物，比如 jar 包、war 包、exploded 的文件夹等
* verify 确认：运行检查以验证产物包是否符合质量标准和集成测试结果
* `install 安装`：把产物包安装到 Maven 本地仓库（默认 ~/.m2/repository），以便其它本地项目作为依赖使用（install 主要有两个用途：一对于那些三方库的”本地 jar 包“，我们可以通过 install 把”本地 jar 包“安装到本地仓库，这样就可以在 pom.xml 文件里通过依赖来使用”本地 jar 包“了；二对于那些我们自己开发的项目，就算主项目里是通过公司私有仓库来依赖这些项目的，那这些项目在执行 mvn deploy 之前必然会执行一下 mvn install，这样设计的目的就是直接把这些项目缓存到本地仓库，将来主项目里同步依赖时就不用再去远程费劲下载了）
* `deploy 部署`：把产物包部署到 Maven 远程仓库（中央仓库、公司私有仓库），以便其它项目或开发者作为依赖使用

而每个阶段又可以绑定特定的`插件:目标（Plugin:Goal）`组合来完成该阶段的任务，比如插件 P1 里有两个目标 G1 和 G2， 插件 P2 里有三个目标 G1、G2 和 G3，那么 compile 阶段就可以使用 P1:G1 来完成编译任务，package 阶段就可以组合 P1:G2 和 P2:G1 来完成打包任务，deploy 阶段就可以组合 P2:G2 和 P2:G3 来完成部署任务。比如 compiler 插件里就有三个目标 compile、testCompile、help， 而 compile 阶段就绑定了 compiler:compile 这个插件目标来完成主代码和单元测试代码的编译任务，test 阶段就绑定了 compiler:testCompile 这个插件目标来完成单元测试代码的运行。

#### 2、打包（Package）

> 注意：
>
> * 这里使用 mvn package 这种打包方式，是在使用 Maven 这个打包工具打包
> * 而上一篇使用 Build - Build Artifacts 那种打包方式，是在使用 IDEA 这个 IDE 自带的打包工具打包
> * 也就是说本质上两种打包方式用的打包工具都不一样，只不过两种打包方式的产物输出目录都是 target

这里演示怎么把一个普通 Java 项目打包成一个 runnable jar（普通 jar 是指不能独立运行的 jar 包，里面就是一些 api 供别人调用；runnable jar 是指能独立运行的 jar 包，里面有 main 函数）。

* 首先我们需要将打包方式设置成 jar

  ```XML
  <packaging>jar</packaging>
  ```

* 然后执行 mvn package 命令

  ```shell
  mvn package
  ```

* 然后对打包好的 jar 包执行运行命令

  ```shell
  java -jar /Users/ineyee/Desktop/helloMaven.jar
  ```

* 发现报错 “/Users/ineyee/Desktop/helloMaven.jar中没有主清单属性”，这个报错其实是由两个问题造成的：

  * 第一个问题，我们没有指定程序的入口是哪个类，毕竟 Java 程序的每个类里都可以写 main 函数，那到底哪个类的 main 是入口呢，所以我们需要指定一下
  * 第二个问题，我们解压打包好的 jar 包后会发现其实并没有把三方库的 jar 包给复制一份到产物里面去，也就是说依赖都缺失了，所以我们的 jar 包肯定是无法正常运行的
  * 要解决这两个问题，得在 pom.xml 文件里配置使用一下 spring-boot-maven-plugin 插件

  ```XML
  <!-- 构建信息，比如输出产物的名字、插件配置等 -->
  <build>
    <!-- 输出产物的名字 -->
    <finalName>helloMaven</finalName>
    <!-- 插件配置 -->
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>4.0.0-M2</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>repackage</goal>
            </goals>
            <configuration>
              <!-- 指定程序的入口是哪个类 -->
              <mainClass>Main</mainClass>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
  ```

* 然后重新打包、重新执行，发现 ok 了

#### 3、安装（Install）

这里演示怎么把一个”本地 jar 包“安装到 Maven 本地仓库。

上面四部分 3 小节说依赖的时候，我们其实只说了项目如何依赖远程仓库依赖，而没有说项目如何依赖本地仓库依赖。也就是说实际开发中我们可能会面临这样的问题：并不是所有的依赖都会发布到 Maven 中央仓库或 Maven 公司私有仓库。比如有一个三方库，人家没有把 jar 包发布到 Maven 中央仓库，只提供了直接去人家官网下载 jar 包来使用的方式；又比如我们公司自己的项目打包出来的 jar 包，也没有发布到 Maven 公司私有仓库，只提供了直接使用 jar 包的方式。那我们该怎么在主项目里依赖这类”本地 jar 包“呢？

其实很简单，我们已经知道 Maven 在管理依赖时，其实是先去本地仓库里找有没有依赖的缓存，如果命中的话就直接使用、不会再去远程仓库下载依赖了，所以我们就可以直接把这类”本地 jar 包“安装到本地仓库，让 Maven 在管理依赖时能在本地仓库里命中不就完事了嘛，这样一来我们就可以依旧在 pom.xml 文件里通过 dependency 来依赖这类”本地 jar 包“了。

这里假设我们电脑桌面上已经下载好了一份三方库的”本地 jar 包“——sayhello.jar：

* 我们需要先执行命令把”本地 jar 包“安装到本地仓库

```shell
// mvn install:install-file -Dfile=${jar包的路径} -DgroupId=${公司域名倒写} -DartifactId=${jar包名} -Dversion=${版本} -Dpackaging=jar
mvn install:install-file -Dfile=/Users/ineyee/Desktop/sayhello.jar -DgroupId=com.ineyee -DartifactId=sayhello -Dversion=1.0.0 -Dpackaging=jar
```

* 然后直接去 pom.xml 文件里添加依赖就可以了

```XML
<!-- 某一个依赖（远程仓库没有、本地仓库有） -->
<dependency>
  <groupId>com.ineyee</groupId>
  <artifactId>sayhello</artifactId>
  <version>1.0.0</version>
</dependency>
```

#### 4、部署（Deploy）

jar 包、war 包等格式，部署到中央仓库、公司私有仓库。

#### 5、自动化构建（打包、部署）

可使用 CI/CD 工具（Jenkins、GitLab CI）实现自动化构建。