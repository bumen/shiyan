
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.sdk.weixin.pay;


/**
 * 
 *
 * @date 2018-06-10
 * @author
 */
public class WXPayRequest {
    private WXPayConfig config;

    public WXPayRequest(WXPayConfig config) throws Exception {

        this.config = config;
    }

    public String requestWithoutCert(String urlSuffix, String msgUUID, String reqBody,
            int connectTimeoutMs, int readTimeoutMs, boolean autoReport) throws Exception {
        String domain = config.getWXPayDomain();
        if (domain == null) {
            throw new Exception("WXPayConfig.getWXPayDomain().getDomain() is empty or null");
        }

        String url = "https://" + domain + urlSuffix;

        //String resp = HttpsUtils.postRequest(url, reqBody);

        return "";
    }
}
