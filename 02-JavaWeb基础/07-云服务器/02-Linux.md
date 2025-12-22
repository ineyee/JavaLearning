> Linux 是操作系统内核，不是完整的操作系统，不能给人直接使用
>
> CentOS、Ubuntu 等是基于 Linux 内核的完整操作系统，能给人直接使用
>
> 服务器这台计算机的操作系统 90% 以上安装的都是基于 Linux 内核的操作系统，如 CentOS、Ubuntu 等

## 一、系统目录结构

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

## 二、基础命令操作

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

#### 1、文件和目录操作

* 文件操作

```bash
# =================================
# 创建文件，这里 touch 是创建的意思
touch file.txt
# 拷贝文件到某个目录里，copy
cp file.text dir

# =================================
# 强制删除文件，remove -force
rm -f file.txt

# =================================
# 移动文件到某个目录里，move
mv file.text dir

# =================================
# 读取文件内容，concatenate
cat file.txt
# 实时查看文件末尾新增的内容，tail follow（重要！用于监控应用日志）
tail -f app.log
```

* 目录操作

```bash
# =================================
# 创建目录，make directory
mkdir dir
# 创建嵌套目录，make directory -parents
mkdir -p dir1/dir2
# 递归拷贝一个目录到另一个目录里，copy recursive
cp -r dir1 dir2

# =================================
# 强制递归删除目录，remove -recursive&force
rm -rf file.txt

# =================================
# 移动一个目录到另一个目录里，move
mv dir1 dir2

# =================================
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

# =================================
# 切换目录，change directory
cd /path/to/dir
# 切换到上一层目录
cd ..
# 切换到根目录
cd /
# 切换到当前用户的目录
cd ~
```

#### 2、查找文件和搜索文本

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

#### 3、文件权限管理

```bash
# 修改权限（重要！部署时经常用到）
chmod +x start.sh       # 给脚本添加执行权限
chmod 755 script.sh     # rwxr-xr-x 权限
chown user:group file   # 修改文件所有者

# 理解权限含义：
# r(4)=读, w(2)=写, x(1)=执行
# 755 = rwxr-xr-x = 所有者可读写执行，组和其他人可读执行
```

## 二、进程管理（核心重要！）

```bash
# 查看进程
ps -ef | grep java              # 查找Java进程
ps aux | grep tomcat            # 查看进程详细信息
top                             # 实时查看进程资源占用
htop                            # 更友好的进程查看工具

# 杀死进程
kill -9 PID                     # 强制杀死进程
pkill -f "java.*myapp"          # 按名称杀死进程
killall java                    # 杀死所有Java进程（谨慎！）

# 后台运行（部署应用时常用）
nohup java -jar app.jar > app.log 2>&1 &    # 后台运行并输出到日志
```

## 三、网络相关

```bash
# 查看端口占用（重要！排查端口冲突）
netstat -tlnp | grep 8080       # 查看8080端口占用情况
lsof -i:8080                    # 查看占用8080端口的进程

# 网络连接
curl http://localhost:8080/health   # 测试HTTP接口
wget http://example.com/file.jar    # 下载文件
ping baidu.com                      # 测试网络连通性
telnet host port                    # 测试端口是否开放

# 防火墙
firewall-cmd --list-ports           # 查看开放的端口
firewall-cmd --add-port=8080/tcp    # 开放8080端口
```

## 四、系统资源监控（性能调优必备）

```bash
# CPU和内存
top                     # 实时系统监控
free -h                 # 查看内存使用情况
vmstat 1                # 每秒显示系统状态

# 磁盘
df -h                   # 查看磁盘空间使用情况
du -sh /path            # 查看目录大小
du -h --max-depth=1     # 查看当前目录各子目录大小

# IO监控
iostat -x 1             # 查看IO性能
```

## 五、日志分析（日常工作高频使用）

```bash
# 日志查看技巧
tail -f app.log                              # 实时跟踪日志
tail -n 1000 app.log                         # 查看最后1000行
grep "Exception" app.log | wc -l             # 统计异常数量
grep "2025-12-22" app.log | grep "ERROR"     # 查看今天的错误日志

# 日志分析组合
cat app.log | grep "ERROR" | awk '{print $1}' | sort | uniq -c
# 提取错误日志 -> 提取第一列 -> 排序 -> 统计重复次数
```

## 六、软件安装和环境配置

```bash
# yum/apt包管理（CentOS用yum，Ubuntu用apt）
yum install -y java-1.8.0-openjdk           # 安装JDK
apt-get update && apt-get install mysql-server

# 环境变量配置（重要！配置Java环境）
vim /etc/profile                            # 编辑系统环境变量
export JAVA_HOME=/usr/java/jdk1.8
export PATH=$JAVA_HOME/bin:$PATH
source /etc/profile                         # 使配置生效
```

## 七、Shell 脚本基础

```bash
#!/bin/bash
# 启动应用脚本示例

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

## 八、文本处理三剑客

```bash
# sed - 文本替换
sed -i 's/old/new/g' config.properties      # 批量替换配置
sed -n '100,200p' file                      # 打印100-200行

# awk - 文本分析（强大！）
awk '{print $1,$3}' file                    # 打印第1和第3列
awk -F':' '{print $1}' /etc/passwd          # 以:分隔，打印第1列

# grep - 已在前面介绍
```

## 九、压缩和解压

```bash
# tar打包
tar -czvf app.tar.gz /opt/app/              # 压缩
tar -xzvf app.tar.gz                        # 解压
tar -tzvf app.tar.gz                        # 查看内容

# zip
zip -r app.zip /opt/app/
unzip app.zip
```

## 十、定时任务

```bash
# crontab定时任务（定时备份、清理日志等）
crontab -e                                  # 编辑定时任务
crontab -l                                  # 查看定时任务

# 示例：每天凌晨2点清理7天前的日志
0 2 * * * find /var/log/myapp -name "*.log" -mtime +7 -delete
```

## 核心重点总结

### 日常高频使用（必须熟练）

1. `tail -f` 查看实时日志
2. `ps -ef | grep java` 查找进程
3. `kill -9` 杀进程
4. `netstat/lsof` 查端口
5. `grep` 搜索日志
6. `df -h` / `du -sh` 查磁盘空间
7. `top` / `free` 查资源占用

### 部署相关

- 文件权限管理（chmod/chown）
- 后台运行应用（nohup）
- 编写启动停止脚本
- 环境变量配置

### 故障排查

- 日志分析（grep组合使用）
- 进程状态检查
- 资源监控（CPU、内存、磁盘、网络）

---

**学习建议：** 先重点掌握日常高频使用的命令，在实际工作中逐步深入学习！