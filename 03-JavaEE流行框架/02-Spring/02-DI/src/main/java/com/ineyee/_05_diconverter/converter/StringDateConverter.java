package com.ineyee._05_diconverter.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// 自定义一个类型转换器
// 要求必须实现 Converter 接口，该接口泛型两个类，代表从什么类型转换成什么类型
public class StringDateConverter implements Converter<String, LocalDateTime> {
    // 定义一个属性，让外界传递进来支持哪些日期格式
    private List<String> formats;

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    // 然后重写一下 convert 方法完成转换即可
    // 这里支持多种日期格式，会依次尝试每种格式直到成功
    @Override
    public LocalDateTime convert(String source) {
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDateTime.parse(source, formatter);
            } catch (Exception e) {
                // 继续尝试下一个格式
            }
        }

        throw new IllegalArgumentException("无法将字符串 '" + source + "' 解析为 LocalDateTime，支持的格式: "
                + String.join(", ", formats));
    }
}
