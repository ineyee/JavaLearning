package com.ineyee.controller;

import com.ineyee.pojo.po.Singer;
import com.ineyee.service.SingerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;

@Slf4j
@RestController()
@RequestMapping("/singer")
public class SingerController {
    @Autowired
    SingerService service;

    @GetMapping("/list")
    public String list() {
        log.debug("========000000{}",service.list());
        return "111";
    }
}
