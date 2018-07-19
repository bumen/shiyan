package com.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class MDCoder {

    public static byte[] encodeMD2(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD2");
        return md.digest(data);
    }

    public static byte[] encodeMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(data);
    }


}
