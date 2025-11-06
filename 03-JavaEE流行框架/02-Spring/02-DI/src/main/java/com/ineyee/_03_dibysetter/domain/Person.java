package com.ineyee._03_dibysetter.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Person {
    // 基本数据类型
    private Boolean isPartyMember;
    private Integer age;
    private Double height;

    // BigDecimal
    private BigDecimal salary;

    // 字符串
    private String name;

    // 自定义对象类型
    private Dog dog;

    // 数组
    private List<Person> friendList;

    // Map
    private Map<String, Object> foodMap;

    // Set
    private Set<String> nicknameSet;

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

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public List<Person> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Person> friendList) {
        this.friendList = friendList;
    }

    public Map<String, Object> getFoodMap() {
        return foodMap;
    }

    public void setFoodMap(Map<String, Object> foodMap) {
        this.foodMap = foodMap;
    }

    public Set<String> getNicknameSet() {
        return nicknameSet;
    }

    public void setNicknameSet(Set<String> nicknameSet) {
        this.nicknameSet = nicknameSet;
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
                ", friendList=" + friendList +
                ", foodMap=" + foodMap +
                ", nicknameSet=" + nicknameSet +
                '}';
    }
}
