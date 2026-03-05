#### 3、键值型数据库

代表产品：Redis

特点：

* 数据以键值对的形式存储在内存中，读写速度极快
* 支持多种数据结构（String、List、Hash、Set、ZSet 等）
* 提供持久化机制（RDB 快照、AOF 日志）将内存数据保存到磁盘
* 常用于缓存、会话存储、排行榜、消息队列等场景

举例：以存储用户信息 + 商品信息为例，我们使用键值对来存储，**核心思想是一个键 = 一条数据**

用 JSON 形式来理解 Redis 中的数据结构（注意：只是理解方式，不是真实存储格式）：

```json
**Redis 键命名规范：**
Redis 的键名通常使用冒号 `:` 作为分隔符，格式为 `业务实体:ID` 或 `业务实体:ID:属性`
* `user:1001` 表示 ID 为 1001 的用户（user 是业务实体，表示"用户"这个分类）
* `goods:10001` 表示 ID 为 10001 的商品（goods 是业务实体，表示"商品"这个分类）
* `user:1001:goods` 表示 ID 为 1001 的用户的商品列表

这样命名的好处：
* 结构清晰，一眼就能看出这个键存的是什么数据
* 避免键名冲突（比如用户和商品的 ID 可能重复，加上前缀就不会冲突了）
* 方便使用通配符查询，如 `KEYS user:*` 可以查询所有用户相关的键

// Hash 类型（类似 Map）
# Redis 命令：用户信息（使用哈希表 Hash 存储，Hash 就是 Map 结构）
HSET user:1001 name "张三" sex "男" age 18 address "杭州市上城区"
HSET user:1002 name "李四" sex "女" age 19 address "杭州市拱墅区"
{
  "user:1001": {
    "name": "张三",
    "sex": "男",
    "age": "18",
    "address": "杭州市上城区"
  }
}
{
  "user:1002": {
    "name": "李四",
    "sex": "女",
    "age": "19",
    "address": "杭州市拱墅区"
  }
}

# Redis 命令：商品信息（使用哈希表 Hash 存储）
HSET goods:10001 name "手机" price 5000 desc "很好用的手机" picture "https://xxx.jpg" user_id 1001
HSET goods:10002 name "电脑" price 8000 desc "很好用的电脑" picture "https://xxx.jpg" user_id 1002
{
  "goods:10001": {
    "name": "手机",
    "price": "5000",
    "desc": "很好用的手机",
    "picture": "https://xxx.jpg",
    "user_id": "1001"
  }
}
{
  "goods:10002": {
    "name": "电脑",
    "price": "8000",
    "desc": "很好用的电脑",
    "picture": "https://xxx.jpg",
    "user_id": "1002"
  }
}


// List 类型（类似数组）
# Redis 命令：用户的商品列表（使用列表 List 存储商品 ID）
LPUSH user:1001:goods 10001
LPUSH user:1002:goods 10002
{
  "user:1001:goods": ["10001"]
}
{
  "user:1002:goods": ["10002"]
}
```

> 这里需要注意：
>
> Redis 主要将数据存储在内存中，所以读写速度非常快，但内存容量有限且成本较高，因此 Redis 通常不作为主数据库使用，而是配合 MySQL 或 MongoDB 作为缓存层来提升系统性能
>
> 比如先从 Redis 查询数据，如果没有再从 MySQL/MongoDB 查询，然后将结果缓存到 Redis 中；Redis 还提供了 RDB 和 AOF 两种持久化方式，可以在服务器重启后恢复数据

# Redis 是不是用 JSON 存数据？

Redis 是 Key-Value 型数据库，但这个 Key-Value 不是 JSON，而是是 Redis 自己的数据结构

比如当你执行：

```
HSET user:1001 name 张三 age 18
```

Redis 内部存的是：

```
key = "user:1001"
type = hash
value = {
   name -> 张三
   age  -> 18
}
```

而不是：

```
{
  "user:1001": {
      ...
  }
}
```

