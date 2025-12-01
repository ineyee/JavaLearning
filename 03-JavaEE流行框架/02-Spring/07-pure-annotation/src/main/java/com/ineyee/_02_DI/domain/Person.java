package com.ineyee._02_DI.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Person {
    // 基本数据类型
    private Boolean isPartyMember;
    private Integer age;
    private Double height;

    @Value("false")
    public void setPartyMember(Boolean partyMember) {
        isPartyMember = partyMember;
    }

    @Value("18")
    public void setAge(Integer age) {
        this.age = age;
    }

    @Value("1.88")
    public void setHeight(Double height) {
        this.height = height;
    }

    // BigDecimal
    private BigDecimal salary;

    @Value("666666.66")
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    // 字符串
    private String name;

    @Value("张三")
    public void setName(String name) {
        this.name = name;
    }

    // 自定义对象类型
    private Dog dog;

    @Autowired
    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "isPartyMember=" + isPartyMember +
                ", age=" + age +
                ", height=" + height +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                ", dog=" + dog +
                '}';
    }
}
