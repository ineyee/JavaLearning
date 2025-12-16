package com.ineyee.domain;

public class Product extends BaseDomain {
    public Product() {
    }

    private String name;
    private Double price;
    private String desc;
    // t_product 表里是通过外键 user_id 引用 t_user 表的
    // 那么按照“Domain 的字段必须和数据库表里的字段一一对应”的说法，这里应该定义成 Integer userId; 才对
    // 但实际上，在 Java 代码里我们一般不定义成外键属性，而是直接定义成对象，这样可以使得 Java 代码更加面向对象
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public User getUserBean() {
        return user;
    }

    public void setUserBean(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                ", user=" + user +
                '}';
    }
}
