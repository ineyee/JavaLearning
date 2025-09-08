package com.ineyee.hello_mysql;

public class _0301_概述 {

}

/*
 * 一、数据库驱动库
 * 前面学习 SQL 语句的时候，我们都是在 GUI 工具里执行 SQL 语句的
 * 而在实际开发中我们肯定是在 Java 代码里执行 SQL 语句，所以 JDK 已经给我们提供了操作关系型数据库的 API——JDBC（Java Database Connectivity）
 *
 * 但是 JDBC 只是一套接口
 * 关系型数据库又有很多，如 MySQL、Oracle、PostgreSQL 等
 * 所以各个关系型数据库厂商都实现了 JDBC 接口，我们称其为数据库驱动库
 *
 * 所以我们在 Java 里使用不同的数据库时需要安装不同的数据库驱动库
 * <dependency>
 *     <groupId>com.mysql</groupId>
 *     <artifactId>mysql-connector-j</artifactId>
 *     <version>9.4.0</version>
 * </dependency>
 *
 * <dependency>
 *     <groupId>com.oracle.database.jdbc</groupId>
 *     <artifactId>ojdbc11</artifactId>
 *     <version>23.9.0.25.07</version>
 * </dependency>
 *
 * <dependency>
 *     <groupId>org.postgresql</groupId>
 *     <artifactId>postgresql</artifactId>
 *     <version>42.7.7</version>
 * </dependency>
 *
 * MySQL 在多数情况下会自动处理行级锁，开发者无需手动加锁，比如购票场景多个用户同时去改变余票数值的场景，MySQL 在检测到一个连接在改变值时会加锁，另一个连接必须等待解锁后再改变。当然也有场景需要我们主动加锁来保证数据安全
 */
