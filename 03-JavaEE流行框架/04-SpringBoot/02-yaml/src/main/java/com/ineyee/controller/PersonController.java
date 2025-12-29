package com.ineyee.controller;

import com.ineyee.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@EnableConfigurationProperties(Person.class)
public class PersonController {
    @Bean
    public Person person() {
        Person person = new Person();
        person.setName("${}");
        return person;
    }

    @Autowired
    private Person person;

    @GetMapping("/getPerson")
    public String getPerson() {
        return person.toString();
    }
}
