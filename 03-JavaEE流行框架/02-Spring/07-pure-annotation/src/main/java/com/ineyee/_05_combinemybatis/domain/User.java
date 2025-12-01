package com.ineyee._05_combinemybatis.domain;

// 用户模块接口的表现层之模型层
// 纯粹地存储数据，Domain 的字段必须和数据库表里的字段一一对应
//
// 标准 Java Domain 写法：
// 1、提供一个无参构造方法，不需要自定义构造方法，创建出对象来再一个一个给属性赋值就行了
// 2、属性必须是 private 修饰，保证封装性；为每个属性提供 public 的 getter 和 setter 方法，供外界访问
// 3、重写下 toString 方法，方便打印
public class User extends BaseDomain {
    public User() {
    }

    private String name; // 必传
    private Integer age; // 可选
    private Double height; // 可选
    private String email; // 必传

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", createTime='" + getCreateTime() + '\'' +
                ", updateTime='" + getUpdateTime() + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", email='" + email + '\'' +
                '}';
    }
}
