package com.bmn.sdk.weixin.pay;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class UserWXPayConfigImpl extends WXPayConfig {

    private byte[] certData;
    private static UserWXPayConfigImpl INSTANCE;

    private UserWXPayConfigImpl() {
    }

    public static UserWXPayConfigImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (UserWXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserWXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String getAppID() {
        return "";
    }

    @Override
    public String getMchID() {
        return "";
    }

    @Override
    public String getKey() {
        return "";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    String getWXPayDomain() {
        return WXPayConstants.DOMAIN_API;
    }

}
