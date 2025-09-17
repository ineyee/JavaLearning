package com.ineyee.hello_mysql._03_jdbc.demo.bean;

// 标准 Java Bean 写法：
// 1、属性必须是 private 修饰，保证封装性
// 2、为每个属性提供 public 的 getter 和 setter 方法，供外界访问属性
// 3、提供一个无参构造方法，不需要自定义构造方法，创建出对象来再一个一个给属性赋值就行了
// 4、重写下 toString 方法，方便打印
public class PhoneBean {
    private Integer id;
    private String name;
    private String desc;
    private Double price;
    private String brand;
    private Double score;

    public PhoneBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "PhoneBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                ", brand='" + brand + '\'' +
                ", score=" + score +
                '}';
    }
}
