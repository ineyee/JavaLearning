package com.ineyee._05_diconverter.domain;

import java.time.LocalDateTime;

public class Person {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Person{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
