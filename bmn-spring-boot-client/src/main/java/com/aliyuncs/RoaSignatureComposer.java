package com.aliyuncs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.netty.handler.codec.http.HttpHeaders;
import reactor.netty.http.client.HttpClientRequest;

/**
 * @date 2020-10-22
 * @author zhangyuqiang02@playcrab.com
 */
public class RoaSignatureComposer implements ISignatureComposer {
    private static ISignatureComposer composer = null;
    protected final static String QUERY_SEPARATOR = "&";
    protected final static String HEADER_SEPARATOR = "\n";

    @Override
    public Map<String, String> refreshSignParameters(Map<String, String> parameters,
            Signer signer, String accessKeyId) {
        Map<String, String> immutableMap = new HashMap<String, String>(parameters);
        immutableMap.put("Date", ParameterHelper.getRFC2616Date(null));
        immutableMap.put("Accept", "application/json");
        immutableMap.put("x-acs-signature-method", signer.getSignerName());
        immutableMap.put("x-acs-signature-version", signer.getSignerVersion());
        immutableMap.put("x-acs-signature-nonce", signer.getSignerNonce());
        return immutableMap;
    }

    private String[] splitSubResource(String uri) {
        int queIndex = uri.indexOf("?");
        String[] uriParts = new String[2];
        if (-1 != queIndex) {
            uriParts[0] = uri.substring(0, queIndex);
            uriParts[1] = uri.substring(queIndex + 1);
        } else { uriParts[0] = uri; }
        return uriParts;
    }

    private String buildQueryString(String uri, Map<String, String> queries) {
        String[] uriParts = splitSubResource(uri);
        Map<String, String> sortMap = new TreeMap<String, String>(queries);
        if (null != uriParts[1]) {
            sortMap.put(uriParts[1], null);
        }
        StringBuilder queryBuilder = new StringBuilder(uriParts[0]);
        if (0 < sortMap.size()) {
            queryBuilder.append("?");
        }
        for (Map.Entry<String, String> e : sortMap.entrySet()) {
            queryBuilder.append(e.getKey());
            if (null != e.getValue()) {
                queryBuilder.append("=").append(e.getValue());
            }
            queryBuilder.append(QUERY_SEPARATOR);
        }
        String queryString = queryBuilder.toString();
        if (queryString.endsWith(QUERY_SEPARATOR)) {
            queryString = queryString.substring(0, queryString.length() - 1);
        }
        return queryString;

    }

    protected String buildCanonicalHeaders(HttpHeaders headers, String headerBegin) {
        Map<String, String> sortMap = new TreeMap<String, String>();
        Iterator<Map.Entry<String, String>> it = headers.iteratorAsString();
        while(it.hasNext()) {
            Map.Entry<String, String> e = it.next();
            String key = e.getKey().toLowerCase();
            String val = e.getValue();
            if (key.startsWith(headerBegin)) {
                sortMap.put(key, val);
            }
        }
        StringBuilder headerBuilder = new StringBuilder();
        for (Map.Entry<String, String> e : sortMap.entrySet()) {
            headerBuilder.append(e.getKey());
            headerBuilder.append(':').append(e.getValue());
            headerBuilder.append(HEADER_SEPARATOR);
        }
        return headerBuilder.toString();
    }

    public static String replaceOccupiedParameters(String url, Map<String, String> paths) {
        String result = url;
        for (Map.Entry<String, String> entry : paths.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String target = "[" + key + "]";
            result = result.replace(target, value);
        }

        return result;
    }

    @Override
    public String composeStringToSign(HttpClientRequest request, Signer signer, Map<String, String> queries) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.method()).append(HEADER_SEPARATOR);
        HttpHeaders headers = request.requestHeaders();
        if (headers.get("Accept") != null) {
            sb.append(headers.get("Accept"));
        }
        sb.append(HEADER_SEPARATOR);
        if (headers.get("Content-MD5") != null) {
            sb.append(headers.get("Content-MD5"));
        }
        sb.append(HEADER_SEPARATOR);
        if (headers.get("Content-Type") != null) {
            sb.append(headers.get("Content-Type"));
        }
        sb.append(HEADER_SEPARATOR);
        if (headers.get("Date") != null) {
            sb.append(headers.get("Date"));
        }

        String uri = request.uri();

        sb.append(HEADER_SEPARATOR);
        sb.append(buildCanonicalHeaders(headers, "x-acs-"));
        sb.append(buildQueryString(uri, queries));
        return sb.toString();
    }

    @Override
    public String composeStringToSign(String method, String uri,
            org.springframework.http.HttpHeaders headers, Signer signer,
            Map<String, String> queries) {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append(HEADER_SEPARATOR);
        if (headers.get("Accept") != null) {
            sb.append(headers.get("Accept"));
        }
        sb.append(HEADER_SEPARATOR);
        if (headers.get("Content-MD5") != null) {
            sb.append(headers.get("Content-MD5"));
        }
        sb.append(HEADER_SEPARATOR);
        if (headers.get("Content-Type") != null) {
            sb.append(headers.get("Content-Type"));
        }
        sb.append(HEADER_SEPARATOR);
        if (headers.get("Date") != null) {
            sb.append(headers.get("Date"));
        }

        sb.append(HEADER_SEPARATOR);
        sb.append(buildCanonicalHeaders(headers, "x-acs-"));
        sb.append(buildQueryString(uri, queries));
        return sb.toString();
    }

    protected String buildCanonicalHeaders(org.springframework.http.HttpHeaders  headers, String headerBegin) {
        Map<String, String> sortMap = new TreeMap<String, String>();
        Iterator<Map.Entry<String, List<String>>> it = headers.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, List<String>> e = it.next();
            String key = e.getKey().toLowerCase();
            String val = e.getValue().get(0);
            if (key.startsWith(headerBegin)) {
                sortMap.put(key, val);
            }
        }
        StringBuilder headerBuilder = new StringBuilder();
        for (Map.Entry<String, String> e : sortMap.entrySet()) {
            headerBuilder.append(e.getKey());
            headerBuilder.append(':').append(e.getValue());
            headerBuilder.append(HEADER_SEPARATOR);
        }
        return headerBuilder.toString();
    }

    public static ISignatureComposer getComposer() {
        if (null == composer) {
            composer = new RoaSignatureComposer();
        }
        return composer;
    }
}
