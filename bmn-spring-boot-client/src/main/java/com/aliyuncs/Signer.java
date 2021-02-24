package com.aliyuncs;

/**
 * @date 2020-10-22
 * @author zhangyuqiang02@playcrab.com
 */
public abstract class Signer {
    private final static Signer hmacSHA1Signer = new HmacSHA1Signer();

    public abstract String signString(String stringToSign, AlibabaCloudCredentials credentials);
    public abstract String signString(String stringToSign, String accessKeySecret);
    public abstract String getSignerName();
    public abstract String getSignerVersion();
    public abstract String getSignerNonce();

    public static Signer getSigner(AlibabaCloudCredentials credentials) {
            return hmacSHA1Signer;
    }
}
