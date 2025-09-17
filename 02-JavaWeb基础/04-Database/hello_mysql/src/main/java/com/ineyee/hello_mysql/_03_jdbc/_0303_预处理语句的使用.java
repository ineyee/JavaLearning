package com.ineyee.hello_mysql._03_jdbc;

//import com.mysql.cj.jdbc.Driver;
//import java.sql.DriverManager;

import java.sql.*;

public class _0303_预处理语句的使用 {

    // MySQL 数据库驱动库的类名
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    // jdbc:mysql://：MySQL 的固定写法
    // localhost、3306：域名和端口号
    // db_hello_mysql：数据库的名称
    // serverTimezone=UTC：推荐设置当前会话时区为零时区，以便 MySQL 能正确处理 DATETIME、TIMESTAMP 字段
    //     因为 DATETIME、TIMESTAMP 字段的默认值是获取服务器所在时区的时间，如服务器 1 部署在东八区，服务器 2 部署在西八区， 那么
    //     用户 1 通过东八区的服务器注册时存储在数据库的 createTime 就是东八区的时间 "2025-06-17 17:59:00"、并且没有携带时区信息
    //     用户 2 通过西八区的服务器注册时存储在数据库的 createTime 就是西八区的时间 "2025-06-17 09:59:00"、并且没有携带时区信息
    //     这样在西八区展示东八区用户的 createTime 就会有问题，因为我们不知道 "2025-06-17 17:59:00" 是东八区的、也没法转换为西八区对应的时间
    //     因此我们统一把 DATETIME、TIMESTAMP 字段都设置成零时区的，各个区在拿到时间后转换成自己时区的时间展示即可
    // root、mysqlroot：数据库的用户名和密码
    private static final String URL = "jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mysqlroot";

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
            Class.forName(DRIVER_CLASS_NAME);
            // 3、不写法：
            // 实际上 JDK6 之后，我们可以不用手动注册数据库驱动了，我们只要安装好，DriverManager 就会自动搜索类目录来注册
            // 当然如果在开发中你遇到“无法注册驱动”的错误，那就用自动注册法显示注册一下 ，换句话说不写可能有事、写了肯定没事

            // 第三步：利用 DriverManager 新建一个连接 connection，连接上我们已启动的某个 MySQL 服务器及数据库
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
            try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
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
