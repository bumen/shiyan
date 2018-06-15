package com.bmn.gm.vo;

/**
 * Created by Administrator on 2017/7/24.
 */
public class LoginResult {
    private String success;
    private String username;
    private String pwd; //账户名与密码不匹配，请重新输入, //密码错误，您还可以尝试2次。<a class="flk13" target="_blank" href="//safe.jd.com/findPwd/index.action">点此找回密码</a>
    private String emptyAuthcode;
    private boolean verifycode;

    public boolean getVerifycode() {
        return verifycode;
    }

    public LoginResult setVerifycode(boolean verifycode) {
        this.verifycode = verifycode;
        return this;
    }

    public String getSuccess() {
        return success;
    }

    public LoginResult setSuccess(String success) {
        this.success = success;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public LoginResult setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPwd() {
        return pwd;
    }

    public LoginResult setPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }

    public String getEmptyAuthcode() {
        return emptyAuthcode;
    }

    public LoginResult setEmptyAuthcode(String emptyAuthcode) {
        this.emptyAuthcode = emptyAuthcode;
        return this;
    }
}
