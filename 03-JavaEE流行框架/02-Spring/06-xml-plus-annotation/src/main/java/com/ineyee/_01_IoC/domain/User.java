package com.ineyee._01_IoC.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Component(value = "user")
//@Component("user")
@Scope("singleton")
//@Scope("prototype")
public class User {
}
