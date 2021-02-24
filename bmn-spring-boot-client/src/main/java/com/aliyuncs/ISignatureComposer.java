package com.aliyuncs;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import reactor.netty.http.client.HttpClientRequest;

/**
 * @date 2020-10-22
 * @author zhangyuqiang02@playcrab.com
 */
public interface ISignatureComposer {
    public Map<String, String> refreshSignParameters(Map<String, String> parameters,
            Signer signer, String accessKeyId);

    public String composeStringToSign(HttpClientRequest request, Signer signer,
            Map<String, String> queries);

    public String composeStringToSign(String method, String uri, HttpHeaders request, Signer signer,
            Map<String, String> queries);
}
