package com.ineyee.pojo.po;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "product") // 指定数据库里对应的集合名称，一个集合对应一个 po
public class Product {
    @Id // 用 Id 注解，指定该属性在数据库里对应的字段名为 _id
    private String id;
    @CreatedDate // 自动维护创建时间
    @Field("create_time") // 用 Field 注解，指定该属性在数据库里对应的字段名，默认就是属性名，相同的话可以省略。MongoDB 不会自动下划线转驼峰
    private LocalDateTime createTime;
    @LastModifiedDate // 自动维护更新时间
    @Field("update_time")
    private LocalDateTime updateTime;
    // MongoDB 的软删除需要手动维护 deleted 字段，不像 MyBatis-Plus 那样配置后执行相应的操作就会自动维护
    private Integer deleted;

    private String name;
    private String desc;
    private List<Designer> designerList;
    private BigDecimal price;
}

//  MongoDB 也可以像 MySQL 那样自动维护创建时间和更新时间，使用 spring-boot-starter-data-mongodb 的审计功能：
//
//  1、添加注解
//  public class Product {
//      @CreatedDate  // 自动维护创建时间
//      private LocalDateTime createTime;
//
//      @LastModifiedDate  // 自动维护更新时间
//      private LocalDateTime updateTime;
//  }
//
//  2、在启动类或配置类上启用审计
//  @SpringBootApplication
//  @EnableMongoAuditing  // 启用 MongoDB 审计功能
//  public class Application {
//      public static void main(String[] args) {
//          SpringApplication.run(Application.class, args);
//      }
//  }
//
//  这样一来插入时会自动设置 createTime，更新时会自动更新 updateTime，就像 MySQL 的 CURRENT_TIMESTAMP 和 ON UPDATE CURRENT_TIMESTAMP 那样
