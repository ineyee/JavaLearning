package com.ineyee.hello_mysql._03_jdbc;

//import com.mysql.cj.jdbc.Driver;
//import java.sql.DriverManager;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class _0304_连接池的使用 {
    // 连接池
    private static DataSource connectionPool;

    // “配置文件是什么，我们为什么要使用配置文件”，后面会详细说，这里先按步骤搞就行
    // druid.properties 文件应该创建在 src/main/resources 目录下，这是 Maven 项目的标准结构，src/main/resources 目录专门用于存放配置文件（如 .properties、.xml等）
    // 当项目编译时，该目录下的文件会自动被复制到最终的构建产物（如 WAR 或 JAR 包）的类路径（classpath）中，这样您的 Java 代码就可以通过类加载器轻松访问到这些配置文件了
    //
    // 静态代码块会在类被加载时调用，而且只会调用一次
    static {
        // 用类加载器和输入流读取 Druid 连接池的配置文件 druid.properties
        try (InputStream is = _0304_连接池的使用.class.getClassLoader().getResourceAsStream("druid.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            // 用配置文件创建连接池
            connectionPool = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 第一步：安装  MySQL 的数据库驱动库
        // <dependency>
        // <groupId>com.mysql</groupId>
        // <artifactId>mysql-connector-j</artifactId>
        // <version>9.4.0</version>
        // </dependency>

        try {
            // 第二步：将厂商的数据库驱动 Driver 注册到 JDBC 的驱动管理者 DriverManager
            // 1、手动注册法：
            // 导入 java.sql.DriverManager 和 com.mysql.cj.jdbc.Driver
            // 注册
//            DriverManager.registerDriver(new Driver());
            // 2、自动注册法：
            // 实际开发中一般都是这样注册，JVM 一看到 Class.forName(...) 就会去装在这个类，就会执行类的 static {...} 静态代码块
            // 而数据库驱动 Driver 内部的静态代码块里已经写好了注册流程，为的就是不想让各厂商的数据库驱动污染我们的代码，而是让各厂商的数据库驱动 Driver 托管给 JDBC 的驱动管理者 DriverManager
            // 也就是说我们不需要再导入数据库驱动相关的东西，只需要用字符串写个数据库驱动的类名就可以了，要换数据库驱动也只需要换个字符串而已
//            Class.forName(DRIVER_CLASS_NAME);
            // 3、不写法：
            // 实际上 JDK6 之后，我们可以不用手动注册数据库驱动了，我们只要安装好，DriverManager 就会自动搜索类目录来注册
            // 当然如果在开发中你遇到“无法注册驱动”的错误，那就用自动注册法显示注册一下，换句话说不写可能有事、写了肯定没事
            //
            // 用了连接池的话，就不用显式注册了。因为连接池库内部会去帮我们显示注册，毕竟连接池内部也要用数据库驱动库来自行维护连接

            // 第三步：利用连接池库新建一个连接池，连接上我们已启动的某个 MySQL 服务器及数据库
            // 之前我们操作数据库都是在 try-with-resources 语法里只创建了一个连接，也就是说访问一次数据库就创建一个连接，访问完就自动释放这条连接，下次访问继续创建和释放，但其实对于频繁访问数据库来说，这样的性能是不高的
            // 并且在 MySQL 里同一个连接上的增删改查操作只能串行执行、无法并行执行，这就意味着如果只有一个用户通过我们的服务向我们的数据库发起增删改查操作的话，一个连接是没有问题的；但如果有十个用户同时通过我们的服务向我们的数据库发起查询操作，那一个连接就不够用了，必然会有用户处于等待的状态、等前面的用户查询完了才能轮到给他返回查询结果，这样就导致查询效率非常低
            //
            // 因此，我们就需要创建多个连接来处理多个用户并发查询的场景，并且最好能处理掉连接频繁创建和释放的性能问题
            // 事实上，已经有很多开源的连接池库了，我们直接拿来用就行了，比如 C3P0、Proxool、DBCP、BoneCP、Druid 等，这里我们用阿里的 Druid
            // 连接池刚创建的时候，里面可能没有连接，一旦有访问数据库的操作了，它就会懒加载式地自动创建第一条连接用于访问，访问结束后不会销毁，而是自动放到连接池中，以便后续使用
            // 并且如果连接池中其它连接都处于 busy 状态的时候也会自动创建新连接，等新连接使用完后不会销毁，而是放到连接池中，以便后续使用
            // 所以我们不需要手动去创建很多连接，只需要创建一个连接池就可以了，这样就成功解决了上面两个问题
            //
            // 当然我们也不能无限制地往连接池里创建连接，因为过多的连接也会带来过大的内存开销和资源开销，应用过载的话是有可能导致数据库崩溃的
            // 因此，我们一般都会设置连接池的大小来限制连接的最大数量，通用经验法则：
            //     * 小型应用（低并发，< 100 并发用户）：10~20
            //     * 中型应用（中等并发，100~500 并发用户）：20~50
            //     * 大型应用（高并发，> 500 并发用户，分布式集群）：50~100（需结合数据库配置）
            //
            // 并发是指同一时间有多少用户在访问数据库，举例：
            // 如果 100 个用户在 1 秒钟（1000ms）内依次发出请求，但【每个请求执行时间只有 5ms】，那么峰值同时占用的连接可能只有 1~2 个（因为很快就释放了），那并发就是 1~2
            // 如果 10 个用户在 1 秒钟内发出请求，但【每个 SQL 要跑 1 秒钟】，那么并发就可能接近 10，因为这 10 个连接会同时挂起
            // 所以并发不是“每秒有多少人访问”，而是“同时占用资源的用户数”。一个实用的衡量方式：并发 ≈ QPS × 平均响应时间
            // QPS（Queries Per Second）：每秒请求数
            // 平均响应时间：每个请求占用连接的时长（秒）
            // 比如：
            //     * 每秒大概有 100 个用户请求，每个请求平均 0.05 秒完成，那么并发 ≈ 100 × 0.05 = 5，只需要大概 5 个连接就够了
            //     * 每秒大概有 100 个用户请求，每个请求平均 1 秒完成，那么并发 ≈ 100 × 1 = 100，就需要接近 100 个连接才能不阻塞
            //
            // 连接池的核心工作原理：
            // 假设连接池配置为 connectionLimit: 2，同时有 3 个并发请求，那么：
            // 请求 1 和 2 立即分配到连接 1 和 2
            // 请求 3 进入队列等待，直到前两个连接有某个连接释放，再分配执行
            //
            // 第四步：利用 connection 创建 SQL 预处理语句对象 preparedStatement
            // 之前我们是直接把 SQL 语句先写完整，然后直接塞到 executeXxx() 函数里去执行
            // 但是这样写的话，如果 SQL 语句里包含变量，比如用户 1 要查询 id 大于 3 的歌曲，用户 2 要查询 id 大于 5 的歌曲，那每次执行一条完整的 SQL 语句时，MySQL 都会重新解析和编译一次 SQL 语句，这样查询性能就比较低
            //
            // 而使用了预处理语句的话（编写 SQL 语句时把原来写死的值都换成问号?，将来执行 SQL 语句时把值作为参数传递进去即可），我们其实是将 SQL 语句模板传递给了 MySQL，MySQL 会对这个语句模板进行解析、编译、缓存
            // 而当我们真正执行 SQL 语句时、把参数传递进去，MySQL 就不会再对这条 SQL 语句进行解析和编译了，而是直接执行
            // 所以相当于是解析和编译一次，后续无论执行多少次都不会再解析和编译，所以查询性能更高
            //
            // 此外预处理语句还可以防止 SQL 注入攻击，因为变量值在执行前会被预处理
            //
            // 第六步：关闭资源（关闭预处理语句 preparedStatement、关闭连接 connection）
            // JDK7 之后提供了 try-with-resources 语法———— try(...) {...}（跟 try-catch 是两个独立语法）
            // 对于那些需要关闭的资源对象，我们可以把它们的创建过程放在 try 的小括号里，那么当 try 大括号里的代码执行完毕后，就会自动关闭这些资源对象
            // 而且关闭的顺序跟创建的顺序是倒序的
            String selectSql = """
                    SELECT *
                    FROM t_product
                    WHERE brand = ?;
                    """;
            // 从数据库连接池中获取一个连接，注意连接对象就不要再放到 try 的小括号里了，因为不需要再使用完即自动释放了
            Connection connection = connectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
                // 第五步：利用 preparedStatement 执行 SQL 语句
                // 实际开发中，一般都是先通过 Navicat GUI 工具或命令行先把数据库和表创建好，项目里就只做些增删改查操作，很少在项目里通过代码来创建数据库和表

                // 给 SQL 语句设置实参
                // 第一个参数代表参数的下标，从 1 开始
                // 第二个参数代表参数的值
                preparedStatement.setString(1, "苹果");
                // 使用 executeQuery(...) 方法来执行查操作，返回值为查询到的数据集
                ResultSet selectRet = preparedStatement.executeQuery();
                // ResultSet 的游标刚开始是不指向任何一行的，调用一下 next() 方法就指向下一条数据
                // next() 方法：当指向的那行有数据时，就返回 true，否则返回 false
                while (selectRet.next()) {
                    // 获取指向那行的数据
                    // 注意：这里的 key 不一定是数据库里的字段名，严格来说是查询 SQL 语句里的字段名或别名、因为我们可能在查询 SQL 语句里给字段名取别名
                    System.out.println(selectRet.getInt("id"));
                    System.out.println(selectRet.getString("name"));
                    System.out.println(selectRet.getString("desc"));
                    System.out.println(selectRet.getDouble("price"));
                    System.out.println(selectRet.getString("brand"));
                    System.out.println(selectRet.getDouble("score"));
                    System.out.println("-----------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}