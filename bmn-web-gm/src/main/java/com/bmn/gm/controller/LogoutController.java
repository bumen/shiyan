package com.bmn.gm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/6/26.
 */
@Controller
public class LogoutController {

    @RequestMapping("logout")
    public String logout(HttpServletRequest request) {



        return "login";
    }

}
