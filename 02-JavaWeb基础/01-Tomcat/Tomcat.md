## 一、客户端与服务器的交互过程

![客户端与服务器的交互过程](客户端与服务器的交互过程.png)

服务器是这样的：

* 一台服务器

* 服务器上得安装 JDK，因为 JDK 里包含 JRE、JRE 里又包含 JVM，而服务器软件 Tomcat 本身也是用 Java 写的，它也得靠 JVM 才能运行起来
* 服务器上得安装 Tomcat，因为
  * 对外：Tomcat 负责监听端口
    * 端口不是物理部件，只是一个虚拟概念——即一个端口对应着一个 Tomcat 进程，Tomcat 在启动时肯定会设置监听某个端口（默认 8080），此时两者就形成了绑定关系。Tomcat 和端口是一对多的关系，即一个 Tomcat 可以监听一个或多个端口，但是一个端口只能被一个 Tomcat 监听
    * 端口用 2 个字节的无符号整形来表示，所以一台服务器理论上有 65536 个端口。端口只有在被某个 Tomcat 监听时，才处于“打开”状态；没有被监听的端口，默认都处于“关闭”状态
  * 对内：Tomcat 内部部署着一个或多个 JavaWeb 项目， JavaWeb 项目必须部署到 Tomcat 这样的服务器软件中才能运行起来，因为 JavaWeb 项目自己是没有 main 函数的，所以得依赖 Tomcat 的 main 函数才能运行起来



客户端是这样的：

* 客户端发送一个 HTTP 请求
* 在应用层时是原始数据（请求路径、请求参数等）
* 到了传输层会加上源端口号和目标端口号
* 到了网络层会加上源 IP 地址和目标 IP 地址
* 到了数据链路层会加上源 MAC 地址和目标 MAC 地址
* 到了物理层会加上数据校验相关的东西



客户端与服务器的交互过程是这样的：

* 有了目标 IP 地址，就知道该给哪个服务器发送数据了
* 有了目标 MAC 地址，就知道该给服务器上的哪个网卡发送数据了，网卡收到数据后就会交给操作系统内核
* 有了目标端口号，操作系统内核就会把数据投递到相应的端口，也即投递给监听该端口的 Tomcat 进程
* 有了请求路径，Tomcat 就知道该把请求参数交给哪个 JavaWeb 项目来处理
* 服务器给客户端响应数据时也是同样的道理

## 二、本机安装 Tomcat

Tomcat 依赖于 JDK，而《Java语言基础》那里我们已经安装好了 JDK，这里直接我们安装一下 Tomcat：

* Tomcat 下载地址：https://tomcat.apache.org/
* 这里选择下载 Tomcat 11
* 下载完双击解压，把解压后的文件夹拖动到你想要的目录下就算安装完成了，这里选择安装在跟 JDK 一样的目录 /Library/Java/apache-tomcat-11.0.10（/ 代表根目录 Macintosh HD，~/ 代表当前用户目录 /Users/ineyee）
* 在 .bash_profile 里配置一下环境变量：export PATH="/Library/Java/apache-tomcat-11.0.10/bin:$PATH"，并执行 source ~/.bash_profile 来让修改立即生效
* Tomcat 没有 tomcat --version 或 tomcat -v 之类的命令来验证是否安装成功
* 然后把我们自己安装的 Tomcat 集成到 IDEA 里：
  * IDEA - Settings - Build, Execution, Deployment - Application Servers
  * New - Tomcat Server - Tomcat Home
  * Apply - OK

## 三、本机启动和关闭 Tomcat

```shell
cd /Library/Java/apache-tomcat-11.0.10/bin

// 给 bin 目录下的所有 .sh 文件加执行权限
chmod +x *.sh
```

#### 方式一：执行脚本文件 catalina

```shell
cd /Library/Java/apache-tomcat-11.0.10/bin

// 执行脚本文件
bash catalina.sh run
```

```shell
cd /Library/Java/apache-tomcat-11.0.10/bin

// 执行脚本文件
bash catalina.sh stop
```

#### 方式二：执行脚本文件 startup、shutdown

```shell
cd /Library/Java/apache-tomcat-11.0.10/bin

// 执行脚本文件（本质就是在调用 catalina）
bash startup.sh
```

```shell
cd /Library/Java/apache-tomcat-11.0.10/bin

// 执行脚本文件（本质就是在调用 catalina）
bash shutdown.sh
```

#### 方式三：通过 IDEA 来启动和关闭 Tomcat

通过 IDEA 来启动和关闭 Tomcat，得有 JavaWeb 项目/模块：

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

***

* 然后我们在 webapp 目录下自定义创建一个 login.html
* 然后在 Current File - Edit Configurations - Add New - Tomcat Server - Local - IDE 会自动检测到我们安装在本机的 Tomcat，大多数配置我们不需要修改，只需要修改部分配置

* Server - HTTP port，由默认的 8080 换成自定义的端口比如 9999（本机上可能有其它软件已经在监听 8080 端口了，会导致 Tomcat 无法监听；${TOMCAT_HOME}/conf/server.xml 文件，\<Connector\> 标签里也能修改端口）
* Deployment - Add - Artifact - ${项目名}:war exploded 或 ${项目名}:war
  * ${项目名}:war exploded 代表不打包成 war 包，直接把项目以展开的文件夹形式部署到 Tomcat 上去，适用于开发阶段。也就是说我们在电脑上开发项目的时候适合用这个，因为开发阶段我们要频繁修改代码重新部署来看效果，这样一来就不用每次都打成压缩包，效率更快，换言之就是热部署效率更高
  * ${项目名}:war 代表把项目打包成 war 包，再部署到 Tomcat 上去，适用于发布阶段。也就是说我们在部署到正式或测试服务器的时候适合用这个，因为发布阶段用压缩包，文件传输速率和文件完整性都更容易得到保证
* `把下面的 Application context 由 “/hello_tomcat_war_exploded”或”/hello_tomcat_war“这么长的一串换成自定义的“/helloTomcat”（注意前面的 / 不能少，这个应用上下文就是 Tomcat 用来查找对应的项目的）`

* Apply - OK

***

* 点击 Run 或 Debug 就可以启动 Tomcat 了，Tomcat 就会把我们的 JavaWeb 项目给自动部署好（`要么部署的是文件夹，要么部署的是 war 包，这里用的其实就是下面”本机 Tomcat 部署 JavaWeb 项目的方式三“`）
* 然后我们去浏览器里通过“http://localhost:9999/helloTomcat”来访问，项目默认返回的是 webapp 目录下的 index.jsp 文件，我们还可以通过“http://localhost:9999/helloTomcat/login.html”来访问我们自己创建的登录页面
* 点击 Stop 就可以关闭 Tomcat
* 其实我们可以在 Tomcat 11.0.10 - Edit Configurations - Startup/Connection 里看到 IDEA 启动和关闭 Tomcat 本质也是在调用 catalina
* 此外我们还可以在 Tomcat 11.0.10 - Edit Configurations - Server - On 'Update' action 来设置更新 Tomcat 默认是重新部署还是重启服务器等

## 四、本机 Tomcat 部署 JaveWeb 项目的方式

#### 方式一：把 JavaWeb 项目打包后的文件夹，直接放在 ${TOMCAT_HOME}/webapps 目录下

* Build - Build Artifacts - ${项目名}:war exploded（代表不打包成 war 包，直接把项目以展开的文件夹形式部署到 Tomcat 上去，适用于开发阶段）
* 这样对 JaveWeb 项目打包后，产物是一个文件夹：${项目名}，放在 target 目录下
* 我们需要把产物文件夹名改成 ${Application context}，以便将来访问
* 把产物文件夹直接复制到 ${TOMCAT_HOME}/webapps  目录下
* 启动 Tomcat
* 然后我们去浏览器里通过“http://localhost:9999/helloTomcat”来访问，项目默认返回的是 webapp 目录下的 index.jsp 文件，我们还可以通过“http://localhost:9999/helloTomcat/login.html”来访问我们自己创建的登录页面，验证是否部署成功

```
缺点：要复制一堆文件夹和文件，万一漏了就出错了，而且这堆文件夹和文件的体积也可能比较大，复制起来会比较慢
```

#### 方式二：把 JavaWeb 项目打包后的 war 包，直接放在 ${TOMCAT_HOME}/webapps 目录下

```
war 包类似于 jar 包，都是压缩文件。war 包是 JavaWeb 项目的包，jar 包是普通 Java 项目的包
```

* Build - Build Artifacts - ${项目名}:war（代表把项目打包成 war 包，再部署到 Tomcat 上去，适用于发布阶段）
* 这样对 JaveWeb 项目打包后，产物是一个 war 压缩包：${项目名}.war，放在 target 目录下
* 我们需要把产物 war 包名改成 ${Application context}.war，以便将来访问
* 把产物 war 包直接复制到 ${TOMCAT_HOME}/webapps  目录下
* 启动 Tomcat
* 然后我们去浏览器里通过“http://localhost:9999/helloTomcat”来访问，项目默认返回的是 webapp 目录下的 index.jsp 文件，我们还可以通过“http://localhost:9999/helloTomcat/login.html”来访问我们自己创建的登录页面，验证是否部署成功

```
优点：只需要复制一个 war 压缩包，不容易漏，复制起来也比较快
缺点：还是得复制来复制去
```

#### 方式三：在 ${TOMCAT_HOME}/conf/Catalina/localhost 文件夹下创建一个 xml 文件，xml 文件名为 ${Application context}

* Build - Build Artifacts - ${项目名}:war exploded 或 ${项目名}:war

* 这样对 JaveWeb 项目打包后，产物是一个文件夹：${项目名} 或 war 压缩包：${项目名}.war，放在 target 目录下

* 我们需要把产物文件夹名改成 ${Application context} 或把产物 war 包名改成 ${Application context}.war，以便将来访问

* 把产物文件夹或 war 包放到指定的路径，这里假设我们的产物文件夹或 war 包放在桌面上

* 在 ${TOMCAT_HOME}/conf/Catalina/localhost 文件夹下创建一个 xml 文件，xml 文件名为 ${Application context}，以便将来访问

* 在 xml 文件里新建一个标签

  ```XML
  # 这里假设我们的产物文件夹放在桌面上: 
  # <Context docBase="/Users/yiyi/Desktop/helloTomcat" />
  # <Context docBase="/Users/yiyi/Desktop/helloTomcat.war" />
  <Context docBase="产物文件夹或 war 包的路径" />
  ```
  
* 启动 Tomcat

* 然后我们去浏览器里通过“http://localhost:9999/helloTomcat”来访问，项目默认返回的是 webapp 目录下的 index.jsp 文件，我们还可以通过“http://localhost:9999/helloTomcat/login.html”来访问我们自己创建的登录页面，验证是否部署成功

  ```XML
  优点：产物不需要复制来复制去
  
  其实我们通过 IDEA 来启动 Tomcat，Tomcat 就是按这种方式部署 JavaWeb 项目的
  启动 Tomcat 后，控制台可以看到一个 CATALINA_BASE 的路径，复制并前往这个路径，进入 /conf/Catalina/localhost，就能看到 ${Application context}.xml 文件了，它里面 Context 标签的 docBase 就指向我们项目里 target 目录下的产物文件夹
  <Context docBase="/Users/yiyi/Desktop/JavaLearning/02-JavaWeb基础/01-Tomcat/hello-tomcat/target/hello-tomcat" />
  ```


