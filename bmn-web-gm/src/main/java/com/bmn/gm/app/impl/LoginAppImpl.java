package com.bmn.gm.app.impl;

import com.bmn.gm.app.LoginApp;
import com.bmn.gm.doman.LoginConnect;
import com.bmn.gm.util.StringUtil;
import com.bmn.gm.vo.LoginResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/7/27.
 */
public class LoginAppImpl implements LoginApp {
    private static final String admin = "qvp";
    private static final String pwd = "123123";
    private Map<Long, LoginConnect> loginMap = new ConcurrentHashMap<>();

    @Override
    public void preLogin(long loginId) {
        LoginConnect connect = loginMap.get(loginId);
        if(connect == null) {
            connect = new LoginConnect();
            connect.setLoginId(loginId);
            loginMap.put(loginId, connect);
        }

        connect.clear();
    }

    @Override
    public LoginResult login(long loginId, String loginname, String password, String authcode) {
        LoginConnect connect = loginMap.get(loginId);
        if(connect == null) {
            preLogin(loginId);
            connect = loginMap.get(loginId);
        }

        LoginResult result = new LoginResult();

        if (StringUtil.isEmplty(loginname) || !loginname.equals(admin)) {
            result.setUsername("账户名不存在，请重新输入");

            failLogin(connect, result);
            return result;
        }

        if(StringUtil.isEmplty(password) || !password.equals(pwd)) {
            result.setPwd("账户名与密码不匹配，请重新输入");

            failLogin(connect, result);
            return result;
        }

        String verifyAuthcode = connect.getAuthCode();
        if(!StringUtil.isEmplty(verifyAuthcode)) {
            if (StringUtil.isEmplty(authcode) || !authcode.equalsIgnoreCase(verifyAuthcode)) {
                result.setEmptyAuthcode("验证码不正确或验证码已过期");
                return result;
            }
        }

        result.setSuccess("hello");
        return result;
    }

    @Override
    public LoginConnect getLoginConnect(long loginId) {
        return loginMap.get(loginId);
    }

    @Override
    public void saveLoginConnect(long loginId, LoginConnect connect) {
        loginMap.put(loginId, connect);
    }

    private void failLogin(LoginConnect connect, LoginResult result) {
        int failCount = connect.getLoginFailCount();
        connect.setLoginFailCount(++failCount);

        if(failCount == 3) {
            result.setVerifycode(true);
        }
    }

    @Override
    public void postLogin(long loginId) {
        loginMap.remove(loginId);


    }
}
