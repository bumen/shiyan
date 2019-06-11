package com.bmn.spring.boot.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class ShutdownController {

    @RequestMapping("/shutdown")
    public String shutdown() {
        log.debug("success get world");

        return "world boot";
    }


}
