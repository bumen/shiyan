package com.bmn.gm.util;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/7/26.
 */
public class SessionUtil {

    public static <T> T getAttribute(HttpSession session, SessionKey key, T defaultValue) {
        if(session == null) {
            return defaultValue;
        }
        Object obj = session.getAttribute(key.CODE);
        return obj == null ? defaultValue : (T)obj;
    }

    public static <T> void setAttribute(HttpSession session, SessionKey key, T v) {
        if(session == null) {
            throw new NullPointerException("设置 session 属性时，session 为空。");
        }
        session.setAttribute(key.CODE, v);
    }

    public static long getLoginId(HttpSession session) {
        return getAttribute(session, SessionKey.LOGIN_ID, 0l);
    }
}
