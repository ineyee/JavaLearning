## 一、通用命令

###### 查询数据库里数据的总条数

```
DBSIZE
```

###### 查询数据库里所有的 key，生产环境不建议使用，因为会扫描所有的数据，阻塞其它命令的执行

```
KEYS *
```

###### 查询数据库里是否存在某个 key，存在返回 1、不存在返回 0

```
EXISTS $key
```

###### 给某个 key 设置有效时间、单位秒，过期后会自动删除

```
EXPIRE $key $seconds
```

###### 给某个 key 设置永久有效

```
PERSIST $key
```

###### 查询某个 key 的有效时间，>= 0 代表有效秒数、-1 代表永久有效、-2 代表没有这个 key

```
TTL $key
```

###### 查询某个 key 的数据类型

```
TYPE $key
```

## 二、String 命令

```json
// 适合缓存基本数据类型的数据
//
// 布尔型、整型、浮点型都当成字符串往里存即可
// 一个 String 类型的值最大能存储 512M 的数据
{
  "name": "张三",
  "age": 18,
  "height": 1.88,
  "adult": 1
}
```

#### 1、增&改、删、查

###### [SET $key $value NX]、[SET $key $value XX]

```
// SET NX（Not eXists）：key 不存在时才新增、存在时啥也不干（可以避免误覆盖数据）
SET name "张三" NX
SET age 18 NX
SET height 1.88 NX

// SET XX（eXists）：key 存在时才修改、不存在时啥也不干（可以避免误新增数据）
SET adult 0 XX // 0 - false
SET adult 1 XX // 1 - true
```

###### [DEL $key]

```
// 删除成功时返回 1、删除失败时返回 0
DEL name
DEL age
DEL height
DEL adult
```

###### [GET $key]

```
GET name
GET age
GET height
GET adult
```

#### 2、批量增&批量改、批量删、批量查

###### [MSET $key1 $value1 $key2 $value2 ...]

```
// 批量增&改没有 NX|XX 之类的命令
// key 不存在时就新增、存在时就修改
MSET name "李四" age 19 graduate 0
```

###### [DEL $key1 $key2 ...]

```
// DEL 本来就支持批量删除，会返回删除成功的条数
DEL name age height adult
```

###### [MGET $key1 $key2 ...]

```
MGET name age height adult
```

#### 3、字符串常见操作

###### 字符串拼接 [APPEND $key $value]

```
APPEND name "(zhangsan)"
```

###### 字符串截取 [GETRANGE $key $startIndex $endIndex]

```
// [$startIndex, endIndex] 闭区间、前后都包含
GETRANGE name 0 0
```

###### 字符串替换 [SETRANGE $key $offset $value]

```
SETRANGE name 0 z
```

###### 获取字符串长度 [STRLEN $key]

```
STRLEN name
```

#### 4、“整型”专用

###### [INCR $key]、[DECR $key]、[INCRBY $key $n]、[DECRBY $key $n]

```
// +1
INCR age
// -1
DECR age
// +n
INCRBY age 10
// -1
DECRBY age 10
```

#### 5、“浮点型”专用

###### [INCRBYFLOAT $key $n]

```
// +n，n 可以是负数
INCRBYFLOAT height 1
```

## 三、Hash 命令

```json
// 适合缓存对象类型的数据
//
// 不过不能整体操作整个 hash，只能单独操作 hash 里的每一对儿 key-value
{
	"user:id:1001": {
    "name": "张三",
    "age": 18,
    "height": 1.88,
    "adult": 1
  }
}
```

#### 1、增&改、删、查

###### [HSET $key $field $value]

```
// field 不存在时就新增、存在时就修改
HSET user:id:1001 name "zhangsan"
HSET user:id:1001 age 18
HSET user:id:1001 height 1.88
HSET user:id:1001 adult 1
```

###### [HDEL $key $field]

```
// 删除成功时返回 1、删除失败时返回 0
HDEL user:id:1001 name
HDEL user:id:1001 age
HDEL user:id:1001 height
HDEL user:id:1001 adult
```

###### [HGET $key $field]

```
HGET user:id:1001 name
HGET user:id:1001 age
HGET user:id:1001 height
HGET user:id:1001 adult
```

#### 2、批量增&批量改、批量删、批量查

###### [HMSET $key $field1 $value1 $field2 $value2 ...]

```
// field 不存在时就新增、存在时就修改
HMSET user:id:1001 name "zhangsan" age 18 height 1.88 adult 1
```

###### [DEL $key $field1 $field2 ...]

```
// DEL 本来就支持批量删除，会返回删除成功的条数
HDEL user:id:1001 name age height adult
```

###### [HMGET $key $field1 $field2 ...]

```
HMGET user:id:1001 name age height adult
```

#### 3、Hash 常见操作

###### 查询 Hash 里 filed 的总个数 [HLEN $key]

```
HLEN user:id:1001
```

###### 查询 Hash 里所有的 field [HKEYS $key]

```
HKEYS user:id:1001
```

###### 查询 Hash 里是否存在某个 filed，存在返回 1、不存在返回 0 [HEXISTS $key $field]

```
HEXISTS user:id:1001 name
```

#### 4、“整型” field 专用

###### [HINCRBY $key $field $n]

```
// +n，n 可以是负数
HINCRBY user:id:1001 age 1
```

#### 5、”浮点型” field 专用

###### [HINCRBYFLOAT $key $field $n]

```
// +n，n 可以是负数
HINCRBYFLOAT user:id:1001 height -0.1
```

## 四、List 命令

```json
// 适合缓存数组类型的数据（底层其实是双向链表）
//
// 有序、可重复
// 不过因为 Redis 的数据结构是扁平的，没有嵌套能力，所以 List 里只能存储字符串类型
{
  “news:hot”: ["新闻C", "新闻B", "新闻A"]
}
```

#### 1、插入元素、删除元素、修改元素、获取元素

###### [LPUSH $key $value1 $value2 ...]、[RPUSH $key $value1 $value2 ...]

```
// 从左边插入元素
LPUSH news:hot "新闻C" "新闻B" "新闻A"

// 从右边插入元素
RPUSH news:hot "新闻CC" "新闻BB" "新闻AA"
```

###### [LPOP $key]、[RPOP $key]

```
// 从左边删除一个元素
LPOP news:hot

// 从右边删除一个元素
RPOP news:hot
```

###### [LREM $key $count $value]

```
// 删除指定内容的元素
// count = 0：删除所有匹配的元素
// count > 0：从左往右删除 count 个匹配到的元素
// count < 0：从右往左删除 |count| 个匹配到的元素
LREM news:hot 1 "新闻A"
```

###### [LSET $key $index $value]

```
// 修改指定索引的元素，这里的 L 不是 Left 而是 List 
LSET news:hot 0 “新闻000”
```

###### [LINDEX $key $index]

```
// ["新闻A", "新闻B", "新闻C", "新闻CC", "新闻BB", "新闻AA"]
// 左侧开始的 index 依次为：0 1 2 3 4 5
// 右侧开始的 index 依次为：-6 -5 -4 -3 -2 -1
//
// 按索引获取元素，这里的 L 不是 Left 而是 List 

// 获取 index == 0 的元素和获取 index == -6 的元素，其实是同一个元素"新闻A"
LINDEX news:hot 0
LINDEX news:hot -6
```

###### [LRANGE $key $startIndex $endIndex]

```
// 查询指定范围内的元素，这里的 L 不是 Left 而是 List
// [$startIndex, $endIndex] 闭区间、前后都包含，-1 表示最后一个元素

// 查询所有元素
LRANGE news:hot 0 -1
// 查询前 3 和元素
LRANGE news:hot 0 2
```

#### 2、List 常见操作

###### 获取 List 长度 [LLEN $key]

```
LLEN news:hot
```

###### 截取 List [LTRIM $key $startIndex $endIndex]

```
// [$startIndex, $endIndex] 闭区间、前后都包含，-1 表示最后一个元素

// 只保留前 3 个元素，影响的是原 List
LTRIM news:hot 0 2
```

## 五、Set 命令

```json
// 适合缓存集合类型的数据
//
// 无需、不可重复
// 不过因为 Redis 的数据结构是扁平的，没有嵌套能力，所以 Set 里只能存储字符串类型
{
  “user:id:1001:tags”: ["Java", "Redis", "MySQL"]
}
```

#### 1、插入元素、删除元素、获取元素

###### [SADD $key $member1 $member2 ...]

```
// 不存在的元素才会新增，已存在的元素会被忽略，返回实际新增的个数
SADD user:id:1001:tags "Java" "Redis" "MySQL"
```

###### [SREM $key $member1 $member2 ...]

```
// 返回实际删除的个数
SREM user:id:1001:tags "MySQL"
```

###### [SMEMBERS $key]

```
// 查询 Set 里所有的元素（注意：Set 无序，返回顺序不固定）
SMEMBERS user:id:1001:tags
```

#### 2、Set 常见操作

###### 查询 Set 里的元素个数 [SCARD $key]

```
SCARD user:id:1001:tags
```

###### 查询某个元素是否存在 [SISMEMBER $key $member]

```
// 存在返回 1、不存在返回 0
SISMEMBER user:id:1001:tags "Java"
```

###### 随机返回 n 个 member（不删除）[SRANDMEMBER $key n]

```
SRANDMEMBER user:id:1001:tags 3
```

###### 随机返回一个元素（删除）[SPOP $key]

```
SPOP user:id:1001:tags
```

#### 3、集合运算

###### 交集 [SINTER $key1 $key2 ...]

```
// 返回两个用户都拥有的标签
SINTER user:id:1001:tags user:1002:tags
```

###### 并集 [SUNION $key1 $key2 ...]

```
// 返回两个用户合并起来并去重后所有的标签
SUNION user:id:1001:tags user:1002:tags
```

###### 差集（我有、对方没有）[SDIFF $key1 $key2 ...]

```
// 返回 1001 有但 1002 没有的标签
SDIFF user:id:1001:tags user:1002:tags

// 返回 1002 有但 1001 没有的标签
SDIFF user:id:1002:tags user:1001:tags
```
