package com.digest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import org.apache.commons.codec.binary.Hex;
import sun.misc.BASE64Encoder;

public class Test {

    public static void main(String[] args)
        throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String str = "HmacMD5消息摘要";

        byte[] key = MACCoder.initHmacMD5Key();
        byte[] data1 = MACCoder.encodeHmacMD5(str.getBytes(), key);
        byte[] data2 = MACCoder.encodeHmacMD5(str.getBytes(), key);

        // System.out.println();

        byte[] hkey = MACCoder.initHmacSHAKey();
        key = "MRp8LeXoQZr+iCNSeB6RAn84HxdVVJEIWOH4m6vVB5c=".getBytes();
        str = "nonce=Y8z0OWw3dw&payload={\"keywords\":[\"习近平重要讲话\",\"R201809131708\",\"111\"]}&timestamp=1542266727";


        byte[] data3 = MACCoder.encodeHmacSHA(str.getBytes(), key);
        byte[] data4 = MACCoder.encodeHmacSHA512(str.getBytes(), key);

        System.out.println(Arrays.equals(data3, data4));

        String string1 = "nonce=Y8z0OWw3dw&payload={\"keywords\":[\"习近平重要讲话\",\"R201809131708\",\"111\"]}&timestamp=1542266727";

        byte[] data = SHACoder.encodeSHA(string1.getBytes());

        String s = Base64.getEncoder().encodeToString(data3);
        System.out.println(s);

        String uri = "W/R7vWyufVxrIoutZj8C/CMjzVL4=";
        System.out.println(URLEncoder.encode(uri, "utf-8"));

        System.out.println(uri.startsWith("/"));
    }

}
