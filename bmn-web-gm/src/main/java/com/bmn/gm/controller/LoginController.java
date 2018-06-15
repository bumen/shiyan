package com.bmn.gm.controller;

import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/6/26.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 获取登录界面
     * @param session
     * @return
     */
    @RequestMapping("login")
    public String loginPage(HttpSession session) {
       /* long loginId = SessionUtil.getAttribute(session, SessionKey.LOGIN_ID, 0l);
        if(loginId == 0) {
            loginId = FaceContext.getIdGenerator().nextId();
            SessionUtil.setAttribute(session, SessionKey.LOGIN_ID, loginId);
        }

        FaceContext.getLoginApp().preLogin(loginId);*/
        return "login";
    }

    /**
     * 用户名；密码登录
     * @param model
     * @return
     */
    @RequestMapping("loginService")
    @ResponseBody
    public String loginService(HttpSession session, @RequestBody MultiValueMap<String, String> model) {
        String loginname = model.getFirst("loginname");
        String password = model.getFirst("nloginpwd");
        String authcode = model.getFirst("authcode");

        logger.info("username: {}, passwod: {}, authcode: {}", loginname, password, authcode);

       /* long loginId = SessionUtil.getAttribute(session, SessionKey.LOGIN_ID, 0l);
        if(loginId == 0) {
            loginId = FaceContext.getIdGenerator().nextId();
            SessionUtil.setAttribute(session, SessionKey.LOGIN_ID, loginId);
        }

        LoginResult result = FaceContext.getLoginApp().login(loginId, loginname, password, authcode);

        return JSON.toJSONString(result);*/
       return "";
    }








}
