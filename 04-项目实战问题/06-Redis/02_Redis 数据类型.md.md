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

#### 5、“浮点型” field 专用

###### [HINCRBYFLOAT $key $field $n]

```
// +n，n 可以是负数
HINCRBYFLOAT user:id:1001 height -0.1
```
