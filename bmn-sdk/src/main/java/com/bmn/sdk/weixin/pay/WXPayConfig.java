package com.bmn.sdk.weixin.pay;

import java.io.InputStream;

public abstract class WXPayConfig {

    /**
     * 获取 App ID
     *
     * @return App ID
     */
    abstract String getAppID();

    /**
     * 获取 Mch ID
     *
     * @return Mch ID
     */
    public abstract String getMchID();

    /**
     * 获取 API 密钥
     *
     * @return API密钥
     */
    abstract String getKey();

    abstract String getWXPayDomain();

    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    abstract InputStream getCertStream();

    public int getHttpConnectTimeoutMs() {
        return 6 * 1000;
    }

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     *
     * @return
     */
    public int getHttpReadTimeoutMs() {
        return 8 * 1000;
    }
}
