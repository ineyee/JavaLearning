package com.ineyee.pojo.po;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class Product {
    private Long id;
    // insert 的时候自动填充该字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    // insert、update 的时候自动填充该字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
    private String name;
    private String description;
    private BigDecimal price;
}
