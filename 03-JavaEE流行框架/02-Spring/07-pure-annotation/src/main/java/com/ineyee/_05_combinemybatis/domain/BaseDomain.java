package com.ineyee._05_combinemybatis.domain;

import java.time.LocalDateTime;

// 顶级类只能用 public 或 package-private 修饰
//  * public：在项目里的任何地方都能访问
//  * package-private（默认）：在当前包里 + 仅当前包里的子类能访问、子类如果在当前包外是不能访问的
// 这里的 BaseDomain 肯定不限于仅当前包里的子类能访问，所以用 public 修饰
//
// BaseDomain 的使命只是为了抽取所有子类 Domain 的公共属性，没有实例化的必要，所以定义为抽象类，也无需提供构造方法
public abstract class BaseDomain {
    // 数据库每张表里必然存在三个字段：id、create_time、update_time
    // 而一张表对应一组 dao、service、bean、servlet
    // 所以所有子类 Bean 必然存在这三个属性：id、createTime、updateTime
    private Integer id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
