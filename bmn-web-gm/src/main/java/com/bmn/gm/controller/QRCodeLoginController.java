package com.bmn.gm.controller;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/7/27.
 */
public class QRCodeLoginController {

    /**
     * 扫码登录成功
     * @param session
     */
    @RequestMapping("qrcode/login")
    public void login(HttpSession session) {
       /* long loginId = SessionUtil.getLoginId(session);
        LoginConnect connect = FaceContext.getLoginApp().getLoginConnect(loginId);
        connect.setQrcode(200);
        FaceContext.getLoginApp().saveLoginConnect(loginId, connect);*/
    }

    /**
     * 扫码取消登录
     * @param session
     */
    @RequestMapping("qrcode/cancelLogin")
    public void cancelLogin(HttpSession session) {
        /*long loginId = SessionUtil.getLoginId(session);
        LoginConnect connect = FaceContext.getLoginApp().getLoginConnect(loginId);
        connect.setQrcode(205);
        FaceContext.getLoginApp().saveLoginConnect(loginId, connect);*/
    }

    /**
     * 手机扫码
     * @param session
     */
    @RequestMapping("qrcode/scan")
    public void scan(HttpSession session) {
        /*long loginId = SessionUtil.getLoginId(session);
        LoginConnect connect = FaceContext.getLoginApp().getLoginConnect(loginId);
        connect.setQrcode(202);
        FaceContext.getLoginApp().saveLoginConnect(loginId, connect);*/
    }

}
