package com.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class SHACoder {

    public static byte[] encodeSHA(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        return md.digest(data);
    }

    public static byte[] encodeSHA256(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(data);
    }

    public static byte[] encodeSHA384(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        return md.digest(data);
    }

    public static byte[] encodeSHA512(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        return md.digest(data);
    }
}
