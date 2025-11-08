package com.ineyee._05_diconverter.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 自定义一个类型转换器
// 要求必须实现 Converter 接口，该接口泛型两个类，代表从什么类型转换成什么类型
public class StringDateConverter implements Converter<String, LocalDateTime> {
    // 定义一个属性，让外界传递进来能把什么格式的时间字符串转换成 LocalDateTime，而不是写死在这个类里
    private String format;

    public void setFormat(String format) {
        this.format = format;
    }

    // 然后重写一下 convert 方法完成转换即可
    @Override
    public LocalDateTime convert(String source) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(source, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("无法将字符串 '" + source + "' 解析为 LocalDateTime，格式应为: " + format, e);
        }
    }
}
