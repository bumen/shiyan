package com.bmn.gm.doman;

/**
 * Created by Administrator on 2017/7/27.
 */
public class LoginConnect {
    private long loginId;
    private int loginFailCount;
    private int qrcode;
    private String authCode;
    private long qrcodeRefreshTime;

    public long getLoginId() {
        return loginId;
    }

    public void setLoginId(long loginId) {
        this.loginId = loginId;
    }

    public int getLoginFailCount() {
        return loginFailCount;
    }

    public void setLoginFailCount(int loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    public int getQrcode() {
        return qrcode;
    }

    public void setQrcode(int qrcode) {
        this.qrcode = qrcode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public long getQrcodeRefreshTime() {
        return qrcodeRefreshTime;
    }

    public void setQrcodeRefreshTime(long qrcodeRefreshTime) {
        this.qrcodeRefreshTime = qrcodeRefreshTime;
    }

    public void clear() {
        loginFailCount = 0;
        authCode = "";
    }
}
