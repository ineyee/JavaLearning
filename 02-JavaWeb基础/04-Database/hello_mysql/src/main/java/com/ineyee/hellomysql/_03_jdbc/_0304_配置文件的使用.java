package com.ineyee.hellomysql._03_jdbc;

//import com.mysql.cj.jdbc.Driver;
//import java.sql.DriverManager;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

// 一、遇到的问题
// 前面《基本使用》和《预处理语句的使用》时，我们都是把数据库的配置直接写在 .java 文件里
// 比如 DRIVER_CLASS_NAME、URL、USERNAME、PASSWORD 等
// 而我们又知道 .java 文件会先编译成 .class 文件，真正部署到服务器上的是 .class 文件
//
// 那问题就来了，如果项目上线后，我们突然需要修改数据库的配置，比如用户名和密码改了，那怎么办呢？
// 我们只能修改 Java 代码，重新编译成 .class 文件，重新部署、用新的 .class 文件替换旧的
// 我们不能直接打开并修改服务器上的 .class 文件，因为这样做很可能会破坏其二进制结构，导致 JVM 无法识别

// 二、配置文件
// 所以一些经常需要动态修改的值，建议放到配置文件里，而不要写死在 Java 代码里
// 因为配置文件在部署到服务器上后，我们能直接打开并修改它里面的内容，就不用重新编译和部署了
//
// Java 里常见的配置文件有两种：
// properties：适合量小、简单的配置，比如一些简单的 key-value 键值对，文件后缀名为 .properties
// xml：适合量大、复杂的配置，比如一些需要嵌套的，文件后缀名为 .xml
//
// 配置文件应该创建在 src/main/resources 目录下，这是 Maven 项目的标准结构，src/main/resources 目录专门用于存放配置文件（如 .properties、.xml 等）
// 当项目编译时，该目录下的文件会自动被复制到最终的构建产物（如 WAR 或 JAR 包）的类路径（classpath）中，这样您的 Java 代码就可以通过类加载器轻松访问到这些配置文件了
public class _0304_配置文件的使用 {

    private static String driverClassName;
    private static String url;
    private static String username;
    private static String password;

    // 静态代码块会在类被加载时调用，而且只会调用一次
    static {
        // 用类加载器和输入流读取配置文件 database.properties
        try (InputStream is = _0304_配置文件的使用.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties properties = new Properties();
            properties.load(is);

            driverClassName = properties.getProperty("driverClassName");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    // 静态代码块会在类被加载时调用，而且只会调用一次
//    static {
//        // 用类加载器和输入流读取配置文件 database.xml
//        try (InputStream is = _0304_配置文件的使用.class.getClassLoader().getResourceAsStream("database.xml")) {
//            // 检查输入流是否为空
//            if (is == null) {
//                throw new RuntimeException("无法找到配置文件 database.xml");
//            }
//
//            // 创建 DocumentBuilderFactory 和 DocumentBuilder
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//
//            // 解析输入流
//            Document doc = builder.parse(is);
//            doc.getDocumentElement().normalize(); // 可选，规范化文档结构
//
//            // 获取根元素
//            Element root = doc.getDocumentElement();
//
//            // 获取各个配置项的值
//            driverClassName = getTextContentByTagName(root, "driverClassName");
//            url = getTextContentByTagName(root, "url");
//            username = getTextContentByTagName(root, "username");
//            password = getTextContentByTagName(root, "password");
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 可以考虑将异常转换为运行时异常，或进行其他处理
//            throw new RuntimeException("加载数据库配置文件失败", e);
//        }
//    }
//
//    /**
//     * 一个辅助方法，用于从父元素中获取指定标签名的第一个元素的文本内容
//     * @param element 父元素
//     * @param tagName 标签名
//     * @return 文本内容，如果未找到则返回 null
//     */
//    private static String getTextContentByTagName(Element element, String tagName) {
//        NodeList nodeList = element.getElementsByTagName(tagName);
//        if (nodeList.getLength() > 0) {
//            return nodeList.item(0).getTextContent();
//        } else {
//            return null;
//        }
//    }

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
            Class.forName(driverClassName);
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
            try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
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
