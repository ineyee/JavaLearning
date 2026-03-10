## 一、key-value 型数据库

代表产品：Redis

> **Redis** 其实也是一种数据库，只不过它的主要应用场景是**将数据存储在服务器的运行内存 RAM 中**，所以它的读写速度非常快，但是 RAM 的容量通常有限且成本较高，因此 Redis 一般不作为主数据库使用，而是配合 MySQL 或 MongoDB **作为缓存层来提升系统性能**，比如先从 Redis 查询数据，如果没有命中再从 MySQL 或 MongoDB 查询，然后将结果缓存到 Redis 中

特点：

* 数据以键值对的形式存储，但不是 JSON 那种、而是 Redis 自己的数据结构
* 数据支持多种数据类型，如 String、List、Hash、Set、ZSet 等

举例：以存储用户信息 + 商品信息为例

```json
key = "user:1001"
type = Hash // value 的类型为 Hash
value = {
  "name": "张三",
  "sex": "男",
  "age": "18",
  "address": "杭州市上城区"
}

key = "user:1002"
type = Hash
value = {
  "name": "李四",
  "sex": "女",
  "age": "19",
  "address": "杭州市拱墅区"
}

key = "goods:10001"
type = Hash
value = {
  "name": "手机",
  "price": "5000",
  "desc": "很好用的手机",
  "picture": "https://xxx.jpg",
  "user_id": "1001"
}

key = "goods:10002"
type = Hash
value = {
  "name": "电脑",
  "price": "8000",
  "desc": "很好用的电脑",
  "picture": "https://xxx.jpg",
  "user_id": "1002"
}

key = "user:1001:goods"
type = List // value 的类型为 List
value = ["10001"]

key = "user:1002:goods"
type = List
value = ["10002"]

// Redis key 的命名规范："业务实体:ID" 或 "业务实体:ID:属性"
// * "user:1001"：代表 ID 为 1001 的用户（user 是业务实体，表示"用户"这个分类）
// * "goods:10001"：代表 ID 为 10001 的商品（goods 是业务实体，表示"商品"这个分类）
// * "user:1001:goods"：代表 ID 为 1001 的用户的商品列表
```

## 二、本机安装 Redis 和 Navicat

#### 1、本机安装 Redis

Redis Server 跟 Tomcat 一样也是一个服务器软件，它在启动时肯定也会设置监听某个端口（默认 6379）来跟客户端通信（如 redis-cli 命令行工具、各种编程语言的驱动、GUI 工具等），Redis Server 内部又存放着一个或多个 Redis 数据库

* Redis 下载地址：https://download.redis.io/releases/redis-8.6.1.tar.gz（这个下载下来的是 Redis 的 C 语言源码压缩包，所以要先在你的环境里安装 gcc 编译器套件，将来解压后需要手动编译 Redis 的 C 语言源码）

* 这里选择下载 Redis 8.6.1

* 下载完双击解压即可，解压后把解压产物（如 redis-8.6.1 这个文件夹）直接拖到 /usr/local 目录下（即 /usr/local/redis-8.6.1），然后需要手动编译安装（/ 代表根目录 Macintosh HD，~/ 代表当前用户目录 /Users/ineyee）

  * 终端进入 redis 目录：cd /usr/local/redis-8.6.1

  * 编译：make

  * 安装：sudo make install PREFIX=/usr/local/redis-8.6.1，这样就会在 redis-8.6.1 目录下生成一个 bin 目录，里面包含了 redis-server（Redis 服务进程）、redis-cli（Redis 命令行客户端） 等可执行文件

  * 然后在 redis 目录下手动创建一个 data 目录（即 /usr/local/redis-8.6.1/data），用来存放数据库数据

  * 然后在 redis 目录下手动创建一个 log 目录、在 log 目录下再手动创建一个 redis.log 文件（即 /usr/local/redis-8.6.1/log/redis.log），用来存放数据库日志

  * 然后打开 redis 目录下的 redis.conf 配置文件（即 /usr/local/redis-8.6.1/redis.conf），确认配置项

    ```yaml
    # -------------------------------
    # 1️⃣ 网络相关配置
    # -------------------------------
    bind 0.0.0.0    # 只允许本机访问（生产环境需要修改）
    port 6379       # 监听端口（默认 6379）
    
    # -------------------------------
    # 2️⃣ 通用配置
    # -------------------------------
    # 是否后台运行（no=前台运行，yes=后台运行）
    daemonize no
    # 日志文件路径（生产环境需要修改）
    logfile /usr/local/redis-8.6.1/log/redis.log
    # 数据库数量，默认 16 个
    databases 16
    
    # -------------------------------
    # 3️⃣ 持久化相关配置
    # -------------------------------
    # 数据库存放路径（生产环境需要修改）
    dir /usr/local/redis-8.6.1/data
    
    # RDB 持久化配置
    # 900 秒内至少有 1 个 key 被修改则触发 RDB 持久化
    save 900 1
    # 300 秒内至少有 10 个 key 被修改则触发 RDB 持久化
    save 300 10
    # 60 秒内至少有 10000 个 key 被修改则触发 RDB 持久化
    save 60 10000
    
    # AOF 持久化配置
    # 是否开启 AOF 持久化（yes=开启，no=关闭）
    appendonly yes
    # AOF 文件名
    appendfilename "appendonly.aof"
    # AOF 同步策略（always=每次写入都同步，everysec=每秒同步一次，no=由操作系统决定）
    appendfsync everysec
    
    # -------------------------------
    # 4️⃣ 安全相关配置
    # -------------------------------
    # 设置访问密码（生产环境必须设置，必须创建用户后才能使用）
    requirepass Redis666!
    
    # -------------------------------
    # 5️⃣ 内存管理
    # -------------------------------
    # 最大内存限制（生产环境可以根据实际情况调整）
    maxmemory 1gb
    # 内存淘汰策略（allkeys-lru = 在所有 key 里，长时间没使用的优先淘汰）
    maxmemory-policy allkeys-lru
    ```


* 在 .bash_profile 里配置一下环境变量：export PATH="/usr/local/redis-8.6.1/bin:$PATH"，并执行 source ~/.bash_profile 来让修改立即生效

* 终端执行 redis-server --version 或 redis-server -v 来验证是否安装成功

* 不论服务器的时区是多少，Redis 的时间戳始终存储为 Unix 时间戳（秒或毫秒），所以我们不需要像 MySQL 一样修改配置文件来指定时区

* 本机前台启动 Redis 服务，终端里执行：redis-server /usr/local/redis-8.6.1/redis.conf

* 本机前台停止 Redis 服务，终端里执行：ctrl + c

* 添加能访问数据库的账号和密码（对应上面的 4️⃣）


  * 启动 Redis 服务，然后终端里执行：redis-cli -a Redis666!，会自动连接到 redis://localhost:6379

  * 添加一个用户，例如 root 超级管理员角色：

    ```shell
    # root：用户名
    # on：启用用户
    # Redis666!：设置密码
    # allcommands：允许执行所有命令
    # allkeys：允许访问所有 key
    ACL SETUSER root on >Redis666! allcommands allkeys
    ```

  * 查看当前用户

    ```shell
    ACL LIST
    
    1) "user default on sanitize-payload #31f06f3ab9a12cba884c48406969ecd28fab8ba93653270218d2577308d80078 ~* &* +@all"
    2) "user root on sanitize-payload #31f06f3ab9a12cba884c48406969ecd28fab8ba93653270218d2577308d80078 ~* resetchannels +@all"
    ```


  * 关闭掉默认用户，通常没有密码，安全性很低，这样就必须使用刚创建的用户才能访问

    ```shell
    ACL SETUSER default off
    ```

  * 持久化 ACL 配置，否则 Redis 重启后用户会丢失

    ```shell
    # 执行 CONFIG REWRITE，使得 Redis 会把 ACL 用户写入 redis.conf
    CONFIG REWRITE
    # 保存
    ACL SAVE
    ```

  * 停止 Redis 服务，重新启动 Redis 服务（后续就是用账号和密码来连接了 redis-cli --user root -a Redis666!）


#### 2、本机安装 Navicat

###### 2.1 安装 Navicat GUI 工具

* Navicat 下载地址：https://www.macwk.com/soft/navicat-premium
* 这里选择下载：Navicat Premium 16.3.7
* 下载完双击安装即可，macOS 上会默认安装在应用程序

###### 2.2 使用 Navicat GUI 工具操作 Redis 数据库

* 打开 Navicat 软件

* 新建一个连接，连接到上面本机启动的 Redis 服务器
* 连接的类型选择为 Redis
* 输入连接的名字，如 redis-local
* 输入域名和端口号，这里 Redis 是本机启动的、端口号也是默认的，所以是 localhost 和 6379
* 输入账号和密码，如 root、Redis666!
* 确定后连接就创建好了，此时连接是灰色，代表连接尚未启动
* 双击这个连接就可以启动了，此时连接会变成红色，这个连接下所有的数据库也会被展示出来
* 接下来我们就可以操作数据库了

## 补充一：系统

#### 1、系统的扩展方案

假设我们有一台 Redis 服务器，当用户量上来以后，这台服务器可能就扛不住了，为了让这台服务器能抗住，我们可以做两件事情：

* 给这台服务器加内存、加硬盘、加 CPU，升级单台服务器的配置我们称之为**垂直扩展**
* 直接加服务器搞成服务器集群，加服务器我们称之为**水平扩展**

#### 2、系统的高可用方案

> 一个高可用系统要达到以下要求：
>
> * 要能避免单点故障问题
> * 要能提高吞吐量 QPS
> * 数据量上来以后要能存得下

###### 2.1 主从服务器 + 哨兵机制

假设我们只有一台 Redis 服务器，那么一旦这台服务器挂掉，整个系统就运转不起来了，这就是单点故障问题。所以我们可以**搞一台 Redis 主服务器、再搞一台或多台 Redis 从服务器，这就是主从服务器，这样一来一旦主服务器挂掉了，Redis 的哨兵机制组件就会检测到，它就会从多台从服务器里选举出一台来作为新的主服务器，并自动完成主从切换，**成功避免单点故障问题。

此外主从服务器还有以下好处：

* 从服务器负责实时备份主服务器上的数据，从而避免数据丢失

* 主服务器只负责写数据操作，从服务器只负责读数据操作，实现读写分离，从而提高“读数据”的吞吐量、但是不能提高“写数据”的吞吐量

###### 2.2 主从服务器分片集群

主从服务器能避免单点故障问题，但是不能提高“写数据”的吞吐量（读数据的确是分流到各个从服务器了，但是写数据还是一台主服务器），也无法保证数据量上来以后能存得下（从服务器只是起到数据备份的作用，而不是起到数据分开存储的作用，也就是说数据还是要完整存一份在主服务器上）。所以我们可以**搞成多主多从，这就是主从服务器分片集群，这样一来写数据也可以分流到不同的主服务器，数据可以分片存储到不同的主服务器、而不是必须完整一份存储到某台主服务器，**成功解决提高吞吐量问题和数据量上来以后要能存得下问题。

#### 3、系统的数据可靠性方案

Redis 的数据是存储在内存中，所以读写速度非常快，但是一旦服务器出现故障，内存中的数据就可能丢失，所以 Redis 还提供了**持久化机制（RDB 快照、AOF 日志）来将内存中的数据持久化到硬盘**，以便在服务器宕机重启后能恢复数据。

## 补充二：Redis 的内存数据淘汰策略

我们知道 Redis 的数据是存储在服务器内存中的，但是整台服务器的内存又不一定是只供 Redis 使用，所以我们就得给 Redis 分配一个它能使用的最大服务器内存，比如 1GB：

| 规模 |    建议分配     |
| :--: | :-------------: |
| 小型 |  256mb ~ 512mb  |
| 中型 |    1gb ~ 2gb    |
| 大型 | 4gb+ 或单独评估 |

当我们给 Redis 分配了最大内存限制后，就得考虑一个问题，那就是内存数据淘汰策略，因为随着数据的增加绝对会超出最大内存限制，超出后应该告诉 Redis 该怎么丢数据：

> LRU means Least Recently Used、最后一次**使用时间** -> 长时间没使用的优先淘汰
> LFU means Least Frequently Used、历史**使用次数** -> 使用总次数少的优先淘汰

|        淘汰策略        |       淘汰范围       | 淘汰算法 |                         说明                          |
| :--------------------: | :------------------: | :------: | :---------------------------------------------------: |
| **noeviction（默认）** |          -           |    -     |   **内存满了直接抛内存溢出异常，拒绝后续的写操作**    |
|      volatile-lru      | 设置了过期时间的 key |   LRU    |   在设置了过期时间的 key 里，长时间没使用的优先淘汰   |
|    **allkeys-lru**     |       所有 key       |   LRU    |       **在所有 key 里，长时间没使用的优先淘汰**       |
|      volatile-lfu      | 设置了过期时间的 key |   LFU    |   在设置了过期时间的 key 里，使用总次数少的优先淘汰   |
|    **allkeys-lfu**     |       所有 key       |   LFU    |       **在所有 key 里，使用总次数少的优先淘汰**       |
|    volatile-random     | 设置了过期时间的 key |   随机   |          在设置了过期时间的 key 里，随机淘汰          |
|     allkeys-random     |       所有 key       |   随机   |                在所有 key 里，随机淘汰                |
|      volatile-ttl      | 设置了过期时间的 key |   TTL    | 在设置了过期时间的 key 里，剩余存活时间最短的优先淘汰 |

