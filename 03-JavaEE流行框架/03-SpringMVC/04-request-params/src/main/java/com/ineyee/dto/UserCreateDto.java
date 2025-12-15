package com.ineyee.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class UserCreateDto {
    // 基本数据类型
    @NotNull(message = "partyMember cant be empty")
    private Boolean isPartyMember;
    private Integer age;
    private Double height;

    // BigDecimal
    private BigDecimal salary;

    // 字符串
    @NotNull(message = "name cant be empty")
    private String name;

    public Boolean getPartyMember() {
        return isPartyMember;
    }

    public void setPartyMember(Boolean partyMember) {
        isPartyMember = partyMember;
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

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserCreateDto{" +
                "isPartyMember=" + isPartyMember +
                ", age=" + age +
                ", height=" + height +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                '}';
    }
}
