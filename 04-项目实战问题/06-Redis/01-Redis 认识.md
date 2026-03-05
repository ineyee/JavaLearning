## 一、key-value 型数据库

代表产品：Redis

> **Redis** 其实也是一种数据库，只不过它的主要应用场景是**将数据存储在服务器的运行内存 RAM 中**，所以它的读写速度非常快，但是 RAM 的容量通常有限且成本较高，因此 Redis 一般不作为主数据库使用，而是配合 MySQL 或 MongoDB **作为缓存层来提升系统性能**，比如先从 Redis 查询数据，如果没有命中再从 MySQL 或 MongoDB 查询，然后将结果缓存到 Redis 中

特点：

* 数据以键值对的形式存储（但不是 JSON 那种，而是 Redis 自己的数据结构），数据支持多种数据类型（如 String、List、Hash、Set、ZSet 等）
* 数据存储在内存中，所以读写速度非常快
* 也提供了持久化机制（RDB 快照、AOF 日志）来将内存数据保存到磁盘，以便在服务器重启后恢复数据

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
