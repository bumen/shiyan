package com.digest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import sun.misc.BASE64Encoder;

public class Test {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        String str = "HmacMD5消息摘要";

        byte[] key = MACCoder.initHmacMD5Key();
        byte[] data1 = MACCoder.encodeHmacMD5(str.getBytes(), key);
        byte[] data2 = MACCoder.encodeHmacMD5(str.getBytes(), key);

        // System.out.println();

        byte[] data3 = MACCoder.encodeHmacSHA512(str.getBytes(), key);
        byte[] data4 = MACCoder.encodeHmacSHA512(str.getBytes(), key);

        System.out.println(Arrays.equals(data3, data4));

        String string1 = "appsecret=dMWOUasdkjasiDKF3lsdfaouDWej5&nonce=Y8z0OWKDNdwnyyZ&timestamp=1542124800";

        byte[] data = SHACoder.encodeSHA(string1.getBytes());

        String s = Base64.getEncoder().encodeToString(data);
        System.out.println(s);
    }

}
