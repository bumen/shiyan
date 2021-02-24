package com.aliyuncs;

/**
 * @date 2020-10-22
 * @author zhangyuqiang02@playcrab.com
 */
public interface AlibabaCloudCredentials {
    /**
     * @return the Access Key Id for this credential
     */
    public String getAccessKeyId();

    /**
     * @return the Access Key Secret for this credential
     */
    public String getAccessKeySecret();
}
