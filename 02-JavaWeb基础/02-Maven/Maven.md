## 一、Maven 是什么

Maven 是一个**包管理工具和项目构建工具**，最初由 Apache 开发，最常用于 Java 项目。

## 二、本机安装 Maven

- Maven 下载地址：https://maven.apache.org/download.cgi
- 这里选择下载 Maven 3.9.11
- 下载完双击解压，把解压后的文件夹拖动到你想要的目录下就算安装完成了，这里选择安装在跟 JDK 一样的目录 Macintosh HD/Library/Java/apache-maven-3.9.11，Maven 默认的本地仓库路径为 ~/.m2

* 在 .bash_profile 里配置一下环境变量：export PATH="/Library/Java/apache-maven-3.9.11/bin:$PATH"，并执行 source ~/.bash_profile 来让修改立即生效
* 终端执行 mvn --version 或 mvn -v 来验证是否安装成功

二、Maven 作为

pom.xml 记录着项目的很多信息，类似于 xxx

包管理工具

1. **依赖声明**

   在 pom.xml 文件里声明依赖，比如：

```
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.30</version>
</dependency>
```



1. Maven 会自动去远程仓库（中央仓库、公司私服等）下载依赖，并缓存到本地仓库（默认 ~/.m2/repository）。

2. **传递依赖**

   

   - 如果 A 依赖 B，B 又依赖 C，那只要声明 A，Maven 会自动把 B、C 全部拉下来。
   - 避免了手动找 JAR 包、处理复杂依赖的痛苦。

   

3. **依赖冲突管理**

   

   - Maven 提供 **依赖树** (mvn dependency:tree) 来查看冲突。
   - 默认使用“最近路径优先”来解决版本冲突，也可以在 pom.xml 里手动指定。

   





------





## **三、Maven 作为** 

## **打包发布工具**





1. **统一构建生命周期**

   Maven 内置三套生命周期：

   

   - **clean**：清理旧编译文件
   - **default**：核心构建（compile、test、package、install、deploy）
   - **site**：生成项目信息网站

   

   例如：

   

   - mvn package → 编译 + 测试 + 打成 JAR/WAR
   - mvn install → 把构建好的 JAR 安装到本地仓库，供其他项目使用
   - mvn deploy → 把构建结果上传到远程仓库（如公司 Nexus、Maven Central）

   

2. **插件机制**

   

   - Maven 本身功能精简，大部分工作由插件完成。

   - 例如：

     

     - maven-compiler-plugin → 编译源码
     - maven-surefire-plugin → 运行单元测试
     - maven-jar-plugin → 打包成 JAR
     - maven-deploy-plugin → 上传到仓库

     

   

3. **打包与发布**

   

   - JAR/WAR/EAR 等格式打包
   - 发布到中央仓库/私服
   - 配合 CI/CD 工具（Jenkins、GitLab CI）实现自动化构建和发布

   





------





## **四、总结**





- **作为包管理工具**：Maven 解决了 JAR 包地狱问题 → 自动下载、传递依赖、管理版本。
- **作为打包发布工具**：Maven 提供标准化构建生命周期 + 插件体系 → 一条命令完成编译、测试、打包、部署。





一句话总结：

👉 **Maven 就是 Java 世界里的 npm + webpack + 发布工具三合一**。