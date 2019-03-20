package com.bmn.spring.boot.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        log.debug("success get hello");
        return "hello boot";
    }
}
