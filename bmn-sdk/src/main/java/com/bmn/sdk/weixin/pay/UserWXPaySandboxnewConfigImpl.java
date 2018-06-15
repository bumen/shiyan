package com.bmn.sdk.weixin.pay;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UserWXPaySandboxnewConfigImpl extends WXPayConfig {

    private WXPayConfig config;

    public UserWXPaySandboxnewConfigImpl(WXPayConfig config) {
        this.config = config;

        this.init();
    }

    private static final String SANDBOX_KEY_URL = " https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";

    public void init() {
        Map<String, String> reqData = new HashMap<>(4);
        reqData.put("mch_id", this.getMchID());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            String sign = WXPayUtil.generateSignature(reqData, this.config.getKey());
            reqData.put("sign", sign);
            String data = WXPayUtil.mapToXml(reqData);

            String r = "";//HttpsUtils.postRequest(SANDBOX_KEY_URL, data);

            reqData = WXPayUtil.xmlToMap(r);

            if (reqData.get("return_code").equals(WXPayConstants.SUCCESS)) {
                this.key = reqData.get("sandbox_signkey");
            }

        } catch (Exception e) {
            WXPayUtil.getLogger().error("获取沙箱环境API验签密钥key 异常", e);
        }

        if (this.key == null) {
            WXPayUtil.getLogger().warn("获取沙箱环境API验签密钥key 失败");
        }
    }

    private String key;

    @Override
    public String getAppID() {
        return config.getAppID();
    }

    @Override
    public String getMchID() {
        return config.getMchID();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public InputStream getCertStream() {
        return config.getCertStream();
    }

    @Override
    String getWXPayDomain() {
        return config.getWXPayDomain();
    }

}
