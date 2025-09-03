## 一、客户端与服务器的交互过程

![客户端与服务器的交互过程](asset/客户端与服务器的交互过程.png)

服务器是这样的：

* 一台服务器

* 服务器上得安装 JDK，因为 JDK 里包含 JRE、JRE 里又包含 JVM，而服务器软件 Tomcat 本身也是用 Java 写的，它也得靠 JVM 才能运行起来
* 服务器上得安装 Tomcat，因为
  * 对外：Tomcat 负责监听端口
    * 端口不是物理部件，只是一个虚拟概念——即一个端口对应一个 Tomcat 进程，Tomcat 在启动时肯定会设置监听某个端口，此时两者就形成了绑定关系。Tomcat 和端口是一对多的关系，即一个 Tomcat 可以监听一个或多个端口，但是一个端口只能被一个 Tomcat 监听
    * 端口用 2 个字节的无符号整形来表示，所以一台服务器理论上有 65536 个端口。端口只有在被某个 Tomcat 监听时，才处于“打开”状态；没有被监听的端口，默认都处于“关闭”状态
  * 对内：Tomcat 内部部署着一个或多个 JavaWeb 项目， JavaWeb 项目必须部署到 Tomcat 这样的服务器软件中才能运行起来



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
* 有了请求路径，Tomcat 就知道该把请求参数交给哪个 Java 项目来处理
* 服务器给客户端响应数据时也是同样的道理

## 二、本机安装 Tomcat

《Java语言基础》那里我们已经安装好了 JDK，这里我们安装一下服务器软件。服务器软件有很多，但用的最多的就是 Tomcat：

* Tomcat 下载地址：https://tomcat.apache.org/
* 这里选择下载 Tomcat 11
* 下载完双击解压，把解压后的文件夹拖动到你想要的目录下就算安装完成了，这里选择安装在跟 JDK 一样的目录 Macintosh HD/Library/Java/apache-tomcat-11.0.10

## 三、Tomcat 操作

`TOMCAT_HOME = Tomcat 的安装目录 = Macintosh HD/Library/Java/apache-tomcat-11.0.10 `

```shell
cd TOMCAT_HOME/bin

// 给 bin 目录下的所有 .sh 文件加执行权限
chmod +x *.sh
```

#### 1、启动和关闭 Tomcat

Tomcat 默认监听 8080 端口来提供 HTTP 服务，所以当 Tomcat 启动成功后，我们可以通过 http://localhost:8080 来访问一下 Tomcat

###### 方式一：执行脚本文件 catalina

```shell
cd TOMCAT_HOME/bin

// 执行脚本文件
bash catalina.sh run
```

```shell
cd TOMCAT_HOME/bin

// 执行脚本文件
bash catalina.sh stop
```

###### 方式二：执行脚本文件 startup、shutdown

```shell
cd TOMCAT_HOME/bin

// 执行脚本文件（本质就是在调用 catalina）
bash startup.sh
```

```shell
cd TOMCAT_HOME/bin

// 执行脚本文件（本质就是在调用 catalina）
bash shutdown.sh
```

###### 方式三：把 Tomcat 集成到 IDEA 里，通过 IDEA 来启动和关闭 Tomcat

把 Tomcat 集成到 IDEA 里：

* IDEA - Settings... - Build, Execution, Deployment - Application Servers
* New - Tomcat Server - Tomcat Home
* Apply - OK

通过 IDEA 来启动和关闭 Tomcat，得有 JavaWeb 项目/模块：

* IDEA - New Project - Jakarta EE（原来的 Java Enterprise）
* Name：hello-tomcat
* Location：项目所在父目录
* Template：Web application（JavaWeb 项目）
* Application server：Tomcat 11.0.10（我们刚才集成的）
* Language：Java
* Build system：Maven
* Group（公司域名倒写）：com.ineyee
* Artifact（默认就是项目名）
* JDK：选择相应版本的 JDK
* Next - Create（这样就创建好了项目）

***

* 然后我们在 webapp 目录下自定义创建一个 login.html
* 然后 Tomcat 11.0.10 - Edit Configurations
  * Server - HTTP port，由 8080 换成自定义的端口比如“9999”（本机上可能有其它软件已经在监听 8080 端口了，会导致 Tomcat 无法监听）
  * Deployment - 选中我们的项目，把下面的 Application context 由 “/hello_tomcat_war_exploded”这么长的一串换成自定义的“/helloTomcat”（注意前面的 / 不能少，这个应用上下文就是 Tomcat 用来查找对应的项目的）
* Apply - OK
* 点击 Run 或 Debug 就可以启动 Tomcat 了，Tomcat 就会把我们的 JavaWeb 项目给自动部署好
* 然后我们去浏览器里通过“http://localhost:9999/helloTomcat”来访问，项目默认返回的是 webapp 目录下的 index.jsp 文件，我们还可以通过“http://localhost:9999/helloTomcat/login.html”来访问我们自己创建的登录页面
* 点击 Stop 就可以关闭 Tomcat

* 其实我们可以在 Tomcat 11.0.10 - Edit Configurations - Startup/Connection 里看到 IDEA 启动和关闭 Tomcat 本质也是在调用 catalina

***

![Edit Configurations-Server](asset/Server.jpg)

![Edit Configurations-Deployment](asset/Deployment.png)

![Edit Configurations-Logs](asset/Logs.png)

![Edit Configurations-CodeCoverage](asset/CodeCoverage.png)

![Edit Configurations-Startup/Connection](asset/StartupConnection.png)