package com.bmn.gm.app;

import com.bmn.gm.doman.LoginConnect;
import com.bmn.gm.vo.LoginResult;

/**
 * Created by Administrator on 2017/7/27.
 */
public interface LoginApp {

    void preLogin(long loginId);

    LoginResult login(long loginId, String loginname, String password, String authcode);

    LoginConnect getLoginConnect(long loginId);

    void saveLoginConnect(long loginId, LoginConnect connect);

    void postLogin(long loginId);
}
