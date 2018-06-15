package com.bmn.gm.controller;

import com.bmn.gm.FaceContext;
import com.bmn.gm.util.SessionUtil;
import com.bmn.gm.vo.ComplexProperty;
import com.bmn.gm.vo.Component;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/7/24.
 */
@Controller
public class HelloController {
    @RequestMapping("hello")
    public String hello(HttpServletRequest request) {
        long loginId = SessionUtil.getLoginId(request.getSession());
        FaceContext.getLoginApp().postLogin(loginId);

        List<Component> accordions = null;
        try {
            accordions = FaceContext.getAuthorityApp().getExplorer(0);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


       // request.setAttribute("accordions", JSON.toJSONString(accordions));

        return "hello";
    }

    @RequestMapping("/combobox")
    @ResponseBody
    public String combobox(HttpServletRequest request) {
        ComplexProperty property = new ComplexProperty();
        property.setId(10);
        property.setText("我的服");

        return "";//JSON.toJSONString(property);
    }





}
