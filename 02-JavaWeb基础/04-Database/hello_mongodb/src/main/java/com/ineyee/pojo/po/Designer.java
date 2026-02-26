package com.ineyee.pojo.po;

import lombok.Data;

// 这个 po 没有对应的集合，因为它是别人的嵌套 po
@Data
public class Designer {
    private String name;
    private Integer age;
    private String sex;
}