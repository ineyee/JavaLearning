package com.ineyee._04_dibyconstructor.domain;

public class Person {
    private Integer age;
    private Double height;

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

    // 全能构造方法，即参数最全的那个构造方法，其它构造方法都可以调用全能构造方法
    Person(int age, double height) {
        this.age = age;
        this.height = height;
    }

    // 部分参数构造方法
    Person(int age) {
        this.age = age;
    }

    // 部分参数构造方法
    Person(double height) {
        this.height = height;
    }

    // 无参构造方法
    Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", height=" + height +
                '}';
    }
}
