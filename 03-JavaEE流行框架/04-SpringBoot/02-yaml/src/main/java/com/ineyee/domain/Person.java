package com.ineyee.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private Boolean isPartyMember;
    private Integer age;
    private Double height;
    private BigDecimal salary;
    private String name;
    private List<String> nicknames;
    private Map<String, Object> foodMap;
}
