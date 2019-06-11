package com.bmn.spring.boot.controller.same;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/20
 */
@Log4j2
@RestController
public class ShutdownController {

    @RequestMapping("/same/shutdown")
    public String shutdown() {
        log.debug("success get world");

        return "world boot";
    }
}
