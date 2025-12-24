> Linux 是操作系统内核，不是完整的操作系统，不能给人直接使用
>
> CentOS、Ubuntu 等是基于 Linux 内核的完整操作系统，能给人直接使用
>
> 服务器这台计算机的操作系统 90% 以上安装的都是基于 Linux 内核的操作系统，如 CentOS、Ubuntu 等

## ✅ 一、系统目录结构

```
├─/(根目录，就是一个 /)
│  ├─bin/(存放系统启动和运行基本命令所需的可执行文件（例如 ls、cp、cat 等），这些命令对所有用户都可用)
│  ├─sbin/(类似于 bin，但存放的命令主要供系统管理员使用（如 /sbin/ifconfig、/sbin/reboot)
│  ├─etc/(存放系统配置文件，如网络配置（/etc/network/interfaces）、服务配置（/etc/nginx/nginx.conf）、用户账户信息（/etc/passwd）等)
│  ├─dev/(存放设备文件，代表系统中的硬件设备（如 /dev/sda 表示硬盘，/dev/tty 表示终端设备）)
│  ├─proc/(虚拟文件系统，提供关于系统运行状态的信息（如 CPU、内存、进程等），数据来源于内存而非磁盘)
│  ├─var/(存放经常变化的文件，如日志文件（/var/log）、邮件队列、数据库文件等)
│  ├─tmp/(存放临时文件，系统重启后通常会被清空)
│  ├─usr/(存放用户安装的程序和数据，结构类似于根目录，包含 /usr/bin（应用程序）、/usr/lib（库文件）、/usr/include（头文件）等)
│  ├─home/(用户主目录的存放位置，每个用户登录后会进入其对应的 /home/用户名 目录，用于保存个人文件和设置)
│  ├─boot/(包含引导系统所需的文件，如内核镜像（vmlinuz）、引导配置文件（grub.cfg）等)
│  ├─lib/(存放系统启动和运行基本命令所需的共享库文件（类似于 Windows 的 DLL 文件）)
│  ├─opt/(用于安装第三方软件包的目录，如某些独立的应用程序（如 Java、Docker）可能会安装在此目录下)
│  ├─mnt/(通常用于临时挂载其他文件系统（如挂载网络存储）)
│  ├─media/(用于挂载可移动设备（如 U 盘、光盘）的挂载点目录)
│  ├─srv/(存放服务相关的数据，例如 Web 服务器的数据文件（如 Apache 的 /srv/www）)
│  ├─root/(系统管理员（root 用户）的主目录，普通用户通常无法访问)
│  ├─run/(存放系统运行时产生的临时文件，如进程 PID 文件)
│  ├─sys/(类似于 /proc，提供设备和内核信息的接口，用于管理设备驱动和硬件信息)
```

## ✅ 二、基础命令操作

> 命令提示符格式：[root@iZbp15mlzb5kylrjts7fakZ ~]#
>
> * root：当前登录用户的用户名
> * @：分隔符
> * iZbp15mlzb5kylrjts7fakZ：当前计算机的名字
> * ~：当前所在的目录或文件，~不是根目录、而是当前用户的目录（这就是 /root 目录），/才是根目录
> * #或$：#代表超级管理员，$代表普通用户
>
>
> 命令格式：命令 [选项] [参数]

#### ✅ 1、文件操作和目录操作

* 文件操作

```bash
# =================================增
# 创建文件，这里 touch 是创建的意思
touch file.txt
# 拷贝文件到某个目录里，copy
cp file.text dir

# =================================删
# 强制删除文件，remove -force
rm -f file.txt

# =================================改
# 移动文件到某个目录里，move
mv file.text dir

# =================================查
# 读取文件内容，concatenate
cat file.txt
# 实时查看文件末尾新增的内容，tail follow（重要！用于监控应用日志）
tail -f app.log
```

* 目录操作

```bash
# =================================增
# 创建目录，make directory
mkdir dir
# 创建嵌套目录，make directory -parents
mkdir -p dir1/dir2
# 递归拷贝一个目录到另一个目录里，copy recursive
cp -r dir1 dir2

# =================================删
# 强制递归删除目录，remove -recursive&force
rm -rf file.txt

# =================================改
# 移动一个目录到另一个目录里，move
mv dir1 dir2

# =================================查
# list，列出当前目录下所有的文件夹和文件（不包括隐藏的）
ls
# list all，列出当前目录下所有的文件夹和文件（包括隐藏的）
ls -a
# list long，列出当前目录下所有文件夹和文件的详细信息，如读写权限、占用内存、修改日期等（不包括隐藏的）
ls -l
# list all long，列出当前目录下所有文件夹和文件的详细信息，如读写权限、占用内存、修改日期等（包括隐藏的）
ls -a -l
# print working directory，显示当前目录的完整路径
pwd

# =================================切换目录
# 切换目录，change directory
cd /path/to/dir
# 切换到上一层目录
cd ..
# 切换到根目录
cd /
# 切换到当前用户的目录
cd ~
```

#### ✅ 2、查找文件和搜索文本

* 查找文件

```bash
# 查找特定名字文件所在的路径
# 如果我们完全不知道这个文件在哪里，可以先 cd 到根目录，从根目录开始找
# 如果我们知道这个文件在某个子目录里，可以先 cd 到这个子目录，从这个子目录开始找，会更快
find -name file.txt # 区分大小写
find -iname file.txt # ignore 忽略大小写

# 查找满足时间条件的所有文件，通常都会先 cd 到指定的目录
# 天数 = -10，modify time 小于 10 天————即 10 天内修改————的文件
# 天数 = +10，modify time 大于 10 天————即 10 天前修改————的文件
# 天数 = 10，modify time 等于 10 天————即第 10 天当天修改————的文件
find -mtime 天数
```

* 搜索文本（重要！用于查找日志中的错误）

```bash
# 在 app.log 文件里搜索包含 ERROR 的行，区分大小写，number
grep -n "ERROR" app.log
# 在 app.log 文件里搜索包含 error 的行，忽略大小写，number ignore
grep -n -i "error" app.log
```

#### ✅ 3、文件权限管理

```bash
# 修改权限，change mode（重要！部署时经常用到）
# 给脚本文件添加执行权限
chmod +x start.s h
# 修改脚本文件的权限
chmod 754 script.sh

# 理解权限含义：
# r(4)=读权限、w(2)=写权限、x(1)=执行权限，总是三个一组
# 7(111) = rwx，代表有读权限、有写权限、有执行权限
# 5(101) = r-x，代表有读权限、没有写权限、有执行权限
# 4(100) = r--，代表有读权限、没有写权限、没有执行权限
#
# 754 = rwxr-xr--
# 第一组 rwx 是针对文件所有者的，代表文件所有者对当前文件的权限
# 第二组 r-x 是针对文件所属用户组的，代表文件所属用户组里所有用户对当前文件的权限
# 第三组 r-- 是针对其他用户的，代表其他用户对当前文件的权限
```

## ✅ 三、压缩与解压缩

```bash
# ================================= zip 包
# 文件
zip 目标压缩文件名.zip 源文件
unzip 要被解压缩的文件名.zip
# 目录，recursive
zip -r 目标压缩目录名.zip 源目录
unzip 要被解压缩的目录名.zip

# ================================= tar.gz 包
# -c = --create，创建一个新的压缩
# -x = --extract，解压
# -z = --gzip，使用 gzip 压缩与解压缩工具
# -v = --verbose，显示详情
# -f = --file，指定压缩后的文件名或目录名
# 文件
tar -czvf 目标压缩文件名.tar.gz 源文件
tar -xzvf 要被解压缩的文件名.tar.gz
# 目录
tar -czvf 目标压缩目录名.tar.gz 源目录
tar -xzvf 要被解压缩的目录名.tar.gz
```

## ✅ 四、系统服务管理

```bash
# 查看当前服务器上都有哪些服务
systemctl list-units --type=service
# 启动某个服务
systemctl start <服务名>
# 关闭某个服务
systemctl stop <服务名>
# 重启某个服务
systemctl restart <服务名>
# 查看某个服务的状态
systemctl status <服务名>

# 查看开机即启动的服务都有哪些
systemctl list-unit-files
# 添加某个服务开机即启动
systemctl enable <服务名>
# 禁止某个服务开机即启动
systemctl disable <服务名>
```

## ✅ 五、进程管理（核心重要！）

```bash
# 查看所有正在运行的进程
ps -ef
# 查看某个进程的详细信息
ps aux | grep tomcat
# 实时查看进程资源占用
top

# 强制杀死进程
kill -9 <进程 ID——PID>

# 后台运行并输出到日志（部署应用时常用）
nohup java -jar app.jar > app.log 2>&1 &
```

## ✅ 六、网络相关

```bash
# 网络连接
ping <ip 地址或域名> # 测试服务器能不能正常访问
telnet <host> <port> # 测试端口是否开放

# 查看端口占用（重要！排查端口冲突）
netstat -tlnp | grep 8080 # 查看8080端口占用情况
lsof -i:8080 # 查看占用8080端口的进程
```

## ✅ 七、日志分析（日常工作高频）

```bash
# 日志查看技巧
tail -f app.log                              # 实时跟踪日志
tail -n 1000 app.log                         # 查看最后 1000 行日志
tail -n 1000 -f app.log                      # 先看最后 1000 行日志，再持续跟随
grep "Exception" app.log | wc -l             # 统计异常数量
grep "2025-12-22" app.log | grep "ERROR"     # 查看今天的错误日志

# 日志分析组合
# 提取错误日志 -> 提取第一列 -> 排序 -> 统计重复次数
cat app.log | grep "ERROR" | awk '{print $1}' | sort | uniq -c
```

## ✅ 八、Shell 脚本基础

```bash
# 指定脚本的解释器，告诉操作系统使用哪个程序来执行脚本
# 如果脚本的第一行是 #!/bin/bash，当您运行脚本时，系统会自动调用 /bin/bash 来执行脚本中的命令
#!/bin/bash

# 变量定义
APP_NAME="myapp"
JAR_FILE="/opt/app/myapp.jar"
LOG_FILE="/var/log/myapp.log"

# 停止旧进程
PID=$(ps -ef | grep $JAR_FILE | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
    echo "Stopping $APP_NAME (PID: $PID)"
    kill -9 $PID
fi

# 启动新进程
echo "Starting $APP_NAME"
nohup java -Xms512m -Xmx2g -jar $JAR_FILE > $LOG_FILE 2>&1 &

echo "Started, PID: $!"
```

## ✅ 九、安装软件和配置环境变量

> 阿里云的实例（即服务器）详情页面里有个“远程连接”，点击它即可连接上服务器，接下来在阿里云给我们打开的终端里操作

#### ✅ 1、服务器安装 JDK（这里我们演示手动安装）

服务器要想运行 Java 程序，必须安装 JDK

- JDK 下载地址：https://www.oracle.com/tw/java/technologies/downloads/，这里选择下载 JDK 21 for Linux（建议和开发时自己电脑上安装的版本保持一致）：https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz
- 下载完成后，把文件上传到 /usr/local/soft 目录下，没有 soft 目录的话就创建一个：mkdir soft
- 上传完成后，把压缩包解压到 /usr/local 目录下（-C 用来指定输出目录）：tar -xzvf /usr/local/soft/jdk-21_linux-x64_bin.tar.gz -C /usr/local
- 解压缩完成后，/usr/local 目录下就能看到类似 jdk-21.0.9 的目录了

* 配置环境变量

  * cd 到 /etc/profile.d 这个目录：cd /etc/profile.d

  * 创建一个专门为 JDK 配置环境变量的脚本文件：touch jdk.sh

  * 打开 jdk.sh 脚本文件，编辑，编辑完记得保存一下

    ```bash
    # 告诉操作系统使用 /bin/bash 来执行脚本中的命令
    #!/bin/bash
    
    # 指定 JDK 的安装目录
    export JAVA_HOME=/usr/local/jdk-21.0.9
    # 指定在终端执行 java 命令时去哪里找命令，优先去 $JAVA_HOME/bin 里找命令，找不到的话再去系统原来的 PATH 里找
    export PATH=$JAVA_HOME/bin:$PATH
    ```

  * 保存完成后，重新加载一下配置文件使其生效：source /etc/profile

* 终端执行 java -version 来验证是否安装并配置成功

#### ✅ 2、服务器安装 Tomcat（这里我们演示手动安装）

* Tomcat 下载地址：https://tomcat.apache.org/，这里选择下载 Tomcat 11（建议和开发时自己电脑上安装的版本保持一致）：https://dlcdn.apache.org/tomcat/tomcat-11/v11.0.15/bin/apache-tomcat-11.0.15.tar.gz
* 下载完成后，把文件上传到 /usr/local/soft 目录下，没有 soft 目录的话就创建一个：mkdir soft
* 上传完成后，把压缩包解压到 /usr/local 目录下（-C 用来指定输出目录）：tar -xzvf /usr/local/soft/apache-tomcat-11.0.15.tar.gz -C /usr/local
* 解压缩完成后，/usr/local 目录下就能看到类似 apache-tomcat-11.0.15 的目录了
* Tomcat 不需要配置环境变量
* 直接 cd 到 /usr/local/apache-tomcat-11.0.15 目录
  * 执行 bin/startup.sh，启动 Tomcat，默认监听 8080 端口
  * 执行 bin/shutdown.sh，关闭 Tomcat

#### ✅ 3、服务器安装 MySQL（这里我们演示 yum 安装）

```bash
# 在 macOS、Windows 上安装软件，我们双击一下安装包就能安装软件了
# 而在 Linux 上安装软件，我们得使用包管理工具来安装软件，CentOS 的包管理工具是 yum，Ubuntu 的包管理工具是 apt
# 这里是 CentOS 和 yum

# 查看已安装的软件
yum list installed
# 搜索某个软件
yum search <软件名>
# 安装某个软件（-y 代表中途有 yes or no 时自动选择 yes）
yum -y install <软件名>
# 卸载某个软件（-y 代表中途有 yes or no 时自动选择 yes）
yum -y remove <软件名>
```

* 前置处理
  * 先把 postfix 和 mariadb-libs 这两个软件卸载掉，不然的话会跟 MySQL 存在依赖冲突：yum -y remove postfix mariadb-libs
  * 然后安装 MySQL 的依赖 net-tools 和 perl：yum -y install net-tools perl
* Yum 仓库及新版 GPG 公钥
  * 为系统安装 MySQL 官方 Yum 仓库：yum localinstall -y https://dev.mysql.com/get/mysql80-community-release-el7-11.noarch.rpm
  * 导入 MySQL 2023 GPG 公钥（关键步骤）：sudo rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023
* 安装 MySQL
  * 查询一下 MySQL 的可用版本：yum list mysql-community-server --showduplicates
  * 这里选择安装 mysql-community-server-8.0.44-1.el7（建议和开发时自己电脑上安装的版本保持一致，没有一致版本的话可以手动安装。注意通过 yum 安装的话，软件没有一个固定的目录，而是按 Linux 标准目录规范分散安装，环境变量也默认放到系统 PATH 里了，我们不需要手动配置环境变量；只有手动安装才可以固定安装目录，才需要配置环境变量）：yum -y install mysql-community-server-8.0.44-1.el7
* 启动 MySQL 服务器软件
  * 执行命令来启动 mysql，默认监听 3306 端口：systemctl start mysqld
  * 然后执行命令来查看 mysql 的运行状态，如果看到 active(running) 就代表启动成功了：systemctl status mysqld
  * 此外我们还可以执行命令来让服务器重启时自动启动 mysql，免得每次重启系统还得我们主动执行命令来启动 mysql：systemctl enable mysqld
* 登录 MySQL
  * 先获取到初始的临时密码：grep 'temporary password' /var/log/mysqld.log
  * 用 root user 和临时密码登录 MySQL：mysql -uroot -p'lsryU5JqZT(T'
  * 登录成功后赶紧修改一个自己能记得住的密码（密码要求：至少8位，包含大小写字母、数字、特殊字符）：ALTER USER 'root'@'localhost' IDENTIFIED BY 'MySQLRoot666!';
  * 以后就用 root user 和自己的密码登录 MySQL：mysql -uroot -p'MySQLRoot666!'
  * 执行命令退出数据库：exit
* 授权数据库的远程连接权限（比如通过 Navicat 连接我们的线上数据库，不授权的话是连不上的）
  * 创建一个用于建立远程连接的用户和密码，也叫 root 和那个密码好了：CREATE USER 'root'@'%' IDENTIFIED BY 'MySQLRoot666!'; 
  * 授予这组用户和密码可以连接所有数据库：`GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;`
  * 授权完成后刷新权限：FLUSH PRIVILEGES;
  * 就可以在 Navicat GUI 工具里连接服务器上的数据库了 
* 修改 mysql 的配置文件
  * 把 MySQL 的默认时区设置为 0 时区
    * 找到并打开 /etc/my.cnf 文件
    * 在 my.cnf 添加“[mysqld]（这里换行）default-time-zone = '+00:00'”，把 MySQL 的默认时区设置为 0 时区，这样才能确保将来通过 SQL 语句自动生成并存储在数据库里的 create_time、update_time 是 0 时区的时间
  * 把 MySQL 的默认编码设置为 utf-8
    * 找到并打开 /etc/my.cnf 文件
    * 在 my.cnf 添加“[mysqld]（这里换行）character_set_server=utf8”，把 MySQL 的默认编码设置为 utf-8
  * 保存文件后，重启一下 mysql：systemctl restart mysqld

###### 在 Navicat GUI 工具里连接服务器上的数据库

- 首先得确保网络和安全组里添加了3306端口，否则连接不上
- 其次得做好授权数据库的远程连接权限，否则连接不了
- 然后就可以去Navicat里连接服务器上的数据库了
  - 新建一个连接
  - 连接的类型选择为 MySQL
  - 输入连接的名字，如 db_mysql
  - 输入域名和端口号，如 8.136.43.114、3306
  - 输入账号和密码，root、前面我们设置的数据库密码
  - 确定后连接就创建好了，此时连接是灰色，代表连接尚未启动
  - 双击这个连接就可以启动了，此时连接会变成绿色，这个连接下所有的数据库也会被展示出来
- 这样我们就可以在Navicat里操作服务器上的数据库了，比如创建表、查看表里的数据等等

###### 在 Navicat GUI 工具里做数据库迁移

比如项目刚开始我们的数据库是创建在自己电脑上的一个数据库，现在项目开发完了，本地数据库里已经有各种表结构及关连了，当然也有一些测试数据，现在要把本地这个数据库迁移到服务器上的数据库里，这件事情其实很简单，Navicat GUI 工具就可以帮我们完成

- 选中本地的数据库
- 右键选中“转储 SQL 文件”，选择“结构和数据”（表结构和测试数据）或“仅结构”（仅表结构），开始转储，等待下载完成
- 在远程服务器上创建一个跟本地同名的数据库
- 然后直接把下载好的 SQL 文件直接拖到远程数据库里，根据提示运行一下即可，这样一来，服务器上的数据库就跟本地的一模一样了

#### ✅ 4、服务器 Tomcat 部署我们的 JavaWeb 项目（一般采用方式三）

```
war 包类似于 jar 包，都是压缩文件。war 包是 JavaWeb 项目的包，jar 包是普通 Java 项目的包
```

* 首先把我们的 JavaWeb 项目搞到服务器上

  * Build - Build Artifacts - ${项目名}:war（代表把项目打包成 war 包，再部署到 Tomcat 上去，适用于发布阶段）

  - 这样对 JaveWeb 项目打包后，产物是一个 war 压缩包：${项目名}.war，放在 target 目录下

  - 我们需要把产物 war 包名改成 ${Application context}.war，以便将来访问

  - 把产物 war 包上传到 /usr/local/soft/${项目名} 目录下，没有 soft/${项目名} 目录的话就创建一个：mkdir soft、mkdir ${项目名}

  * 上传完成后，把压缩包解压到 /usr/local/soft/${项目名} 目录下（-C 用来指定输出目录）：unzip ${项目名}:war

  * 解压缩完成后，/usr/local/soft/${项目名} 目录下就能看到我们的代码了

  * 我们可以去 /usr/local/soft/${项目名}/WEB-INF/classes/dao.properties 配置文件里确认一下，是不是连接的线上数据库，线上数据库的配置对不对，不正确的话可以直接修改配置文件并保存，不用重新打包项目

* 然后把我们的 JavaWeb 项目部署到服务器 Tomcat 上

  * 在 /usr/local/apache-tomcat-11.0.15/conf/Catalina/localhost 文件夹下创建一个 xml 文件，xml 文件名为 ${Application context}，以便将来访问，比如这里就是 ssm.xml，将来如果要部署多个项目就为每个项目都创建一个 xml 文件

  * 在 ssm.xml 文件里新建一个标签：\<Context docBase="产物文件夹或 war 包的绝对路径" /\>

    ```xml
    <Context docBase="/usr/local/soft/ssm" />
    ```

  * 添加完标签以后，我们可以杀掉上一次的 Tomcat，先通过 ps aux | grep tomcat 命令找到 Tomcat 的 PID，然后再通过 kill -9 \<PID> 杀掉进程
  * 然后重启一下 Tomcat， cd 到 /usr/local/apache-tomcat-11.0.15 目录，执行 bin/startup.sh，启动 Tomcat
  * 此时仍然在 /usr/local/apache-tomcat-11.0.15 目录下，我们可以通过 tail -n 1000 logs/catalina.out 命令来查看 Tomcat 是否成功启动并运行我们的 JavaWeb 项目

  - 然后我们去浏览器里通过“http://8.136.43.114:8080/ssm/”来访问，项目默认返回的是 webapp 目录下的 index.jsp 文件，我们还可以通过访问接口来验证是否部署成功