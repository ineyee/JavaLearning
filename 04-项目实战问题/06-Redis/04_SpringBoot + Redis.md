## 一、添加依赖

```xml
<!-- Redis 相关依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!--
	Lettuce 连接池依赖
	SpringBoot 默认使用 Lettuce 作为 Redis 客户端，Lettuce 是基于 Netty 的异步客户端，性能更好。如果要使用连接池，还需要额外引入 commons-pool2 依赖
-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

## 二、创建 yml 配置文件，配置 Redis 连接

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      # redis 里默认有 16 个数据库，要连接哪个数据库，默认 0
      database: 0
      username: root
      password: Redis666!
      lettuce:
        pool:
          # 连接池最大连接数
          max-active: 10
          # 连接池最大空闲连接数
          max-idle: 10
          # 连接池最小空闲连接数
          min-idle: 10
          # 连接池最大阻塞等待时间(ms)，-1 表示没有限制
          max-wait: 10000
```

## 三、自定义 RedisTemplate 的序列化方式

SpringBoot 帮我们配置了两个工具类来操作 Redis：

* RedisTemplate<Object, Object>： **默认，可以操作任意数据类型的数据**
* StringRedisTemplate：**只能操作字符串类型的数据**

但是 RedisTemplate 默认使用的是 JDK 序列化——把 key-value 存进 Redis 时、会转换成二进制数据存进去，这样一来在其它地方（比如 Navicat）看到的数据就是一堆乱码，因此在实际项目中，我们一般会自定义序列化方式：key 使用 String 序列化、value 使用 JSON 序列化

当然如果 Redis 里只存字符串类型的数据，那就没必要自定义序列化方式了，直接使用 StringRedisTemplate 就可以，因为它默认就是用字符串方式存储，不会乱码

```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // key 使用 String 序列化
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // value 使用 JSON 序列化
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
```

## 四、登录、退出登录、其它接口的 token 校验、用户管理操作 => 单 token + redis 缓存方案

#### 1、设计思路和工作流程

> redis 缓冲里的 token 主要负责两件事：
>
> * **登录成功后其它接口的 token 校验**
>
> * **强制踢人、单设备登录这类用户管理操作**



> * 客户端走登录接口
>
> * 服务端判断到登录接口走成功后，服务端用 **UUID 随机生成一个 token，把这个 token 缓存到 redis 中、并把用户信息也缓存到 redis 中，token 和用户信息的的缓存有效期都设置为 7~30 天，**然后把 token 返回给客户端
>   * 这里的 token 就可以不用 JWT Token 了。因为之前没有 redis，所以用户信息必须携带在 token 里传来传去，而现在有 Reids 了，用户信息可以直接缓存在 redis 中不用传来传去。**UUID 随机 token 的体积要比 JWT Token 小很多**
>   * 这里我们设计在 redis 中缓存两个信息：
>     * **key = user\:email\:${email} 或 user\:id\:${id}，value = ${token}，这对 key-value 专门用来做强制踢人、单设备登录这类用户管理操作**
>     * **key = user:token:${token}，value = 关键用户信息的 Hash，这对 key-value 专门用来做登录成功后其它接口的 token 校验**
> * 客户端手动持久化 token
> * 后续客户端走其它接口时，手动把 token 携带到 header 里传递给服务端
> * 服务端可以在这些接口里读取到 header 里的 token，**去 redis 缓存中看看有没有这个 token（不存在或已过期，查询结果都将是没有这个 token）**，没有这个 token 就返回登录凭证无效，否则就返回相应的数据
> * 客户端收到登录凭证无效就跳转登录界面让用户重新登录，收到正常数据就正常使用
> * **客户端退出登录时，需要走一个 logout 接口把 redis 里缓存的 token 删除掉，然后再清除客户端本地持久化的 token、跳转到登录界面**
> * **如果要实现强制下线，直接去 Redis 数据库里根据邮箱或 userId 删除掉某个用户在 redis 里的 token 缓存即可**

#### 2、在登录等接口里生成 token、缓存 token、返回 token 给客户端

#### 3、自定义一个 filter 统一拦截所有接口来验证 token，别忘了配置 filter

#### 4、验证 token 通过后，自定义一个 UserContext 来全局存储 token 里的业务信息

#### 5、各个 Controller 或 Service 里通过 UserContext 获取到 token 里的业务信息，再去操作指定的数据

## 五、Redis 缓存穿透、击穿、雪崩的防护写法

#### 1、缓存穿透

**缓存穿透：**是指查询一个**数据库里根本不存在的数据**。正常的缓存流程是：查 Redis → 没有 → 查 MySQL → 有结果 → 写回 Redis → 下次查 Redis 就有结果了、不必再去查 MySQL。但是如果 MySQL 里也没有，这次查询的结果就不会写进 Redis，下次同样的请求还是会穿过 Redis 直接打到 MySQL，缓存形同虚设

**危害：**如果有人恶意构造大量不存在的 id 发请求，每次都绕过 Redis 直接打 MySQL，MySQL 扛不住就崩了

**常见处理方式：缓存空值（空字符串或 "null"）**

查 MySQL 没结果，就往 Redis 里写一个空值（比如空字符串或 "null"），设一个较短的过期时间（比如 2 分钟）。下次同样的请求查到这个空值，直接返回，不再打 MySQL。缺点是如果攻击者每次用不同的 id，空值缓存也没用，还会污染 Redis 内存

```java
public Object getProduct(Long id) {
    String key = "product:" + id;
    Object value = redisTemplate.opsForValue().get(key);

    if (value != null) {
        // 命中缓存（包括空值缓存）
        return "".equals(value) ? null : value;
    }

    // 查数据库
    Object dbResult = productMapper.selectById(id);
    if (dbResult == null) {
        // 缓存空值（空字符串或 "null"），防止缓存穿透，过期时间设短一点
        redisTemplate.opsForValue().set(key, "", 2, TimeUnit.MINUTES);
        return null;
    }

    redisTemplate.opsForValue().set(key, dbResult, 30, TimeUnit.MINUTES);
    return dbResult;
}
```

#### 2、缓存雪崩

**缓存雪崩**是指**大量缓存 key 在同一时间段集中过期**，导致大量请求同时打到 MySQL。区别在于缓存穿透是针对不存在的数据，雪崩是针对真实存在的数据

**危害：**和缓存穿透类似，MySQL 被大量并发请求压垮

**常见处理方式：过期时间加随机值**

批量写缓存时，在基础过期时间上加一个随机数，让不同 key 的过期时间错开，避免同时失效。基础过期时间 30 分钟 + 随机 0~10 分钟

```java
// 过期时间加随机值，避免同时失效
int baseExpire = 30; // 基础过期时间（分钟）
int randomExpire = new Random().nextInt(10); // 随机 0~9 分钟
redisTemplate.opsForValue().set(key, value, baseExpire + randomExpire, TimeUnit.MINUTES);
```

#### 3、缓存击穿

**缓存击穿**是指**某个热点 key 过期的瞬间**，大量并发请求同时发现缓存没有，全部去查 MySQL。和雪崩的区别是雪崩是大量 key 同时过期，击穿是**单个热点 key** 过期

**危害：**热点 key 说明这个数据访问量极高，过期瞬间可能有成千上万个请求同时穿透到 MySQL，瞬间压垮数据库

**常见处理方式：互斥锁**

发现缓存没有时，不是直接查 MySQL，而是先抢一把分布式锁。抢到锁的那个请求去查 MySQL 并写回缓存，其它没抢到的请求等一会儿重试，重试时缓存已经有值了直接返回、因为先抢到锁的那个请求查完 MySQL 后就会把数据写入缓存中。这样保证同一时间只有一个请求打到 MySQL。缺点是等锁期间有延迟。

```java
// 使用互斥锁（分布式锁）防止缓存击穿
public Object getProductWithLock(Long id) {
    String key = "product:" + id;
    Object value = redisTemplate.opsForValue().get(key);
    if (value != null) {
        return value;
    }

    // 尝试获取分布式锁
    String lockKey = "lock:product:" + id;
    Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
    if (Boolean.TRUE.equals(locked)) {
        try {
            // 双重检查
            value = redisTemplate.opsForValue().get(key);
            if (value != null) return value;

            Object dbResult = productMapper.selectById(id);
            redisTemplate.opsForValue().set(key, dbResult, 30, TimeUnit.MINUTES);
            return dbResult;
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    // 没抢到锁，稍等后重试
    try { Thread.sleep(50); } catch (InterruptedException ignored) {}
    return getProductWithLock(id);
}
```
