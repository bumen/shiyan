package com.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACoderTest {

    private byte[] publicKey;
    private byte[] privateKey;

    public void initKey() throws NoSuchAlgorithmException {
        Map<String, Object> keyMap = RSACoder.initKey();
        publicKey = RSACoder.getPublicKey(keyMap);
        privateKey = RSACoder.getPrivetKey(keyMap);

        System.out.println("公钥：\n"+ Base64.getEncoder().encodeToString(publicKey));
        System.out.println("私钥：\n"+ Base64.getEncoder().encodeToString(privateKey));

    }

    public void test()
        throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String str = "RSA私钥加密";

        byte[] data1 = str.getBytes();
        System.out.println("明文: " + str);

        byte[] encode = RSACoder.encryptByPrivateKey(data1, privateKey);
        byte[] decode = RSACoder.decryptByPublicKey(encode, publicKey);
        System.out.println("加密后：\n" + Base64.getEncoder().encodeToString(encode));
        System.out.println("解密后：\n" + new String(decode));

        str = "RSA公钥加密";
        byte[] data2 = str.getBytes();
        System.out.println("明文：" + str);

        byte[] encode2 = RSACoder.encryptByPublicKey(data2, publicKey);
        byte[] decode2 = RSACoder.decryptByPrivateKey(encode2, privateKey);

        System.out.println("加密后：\n" + Base64.getEncoder().encodeToString(encode2));
        System.out.println("解密后：\n" + new String(decode2));
    }

    public void testSign()
        throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String str = "RSA数字签名";
        byte[] data = str.getBytes();

        byte[] sign = RSACoder.sign(data, privateKey);
        System.out.println("签名：\n" + Base64.getEncoder().encodeToString(sign));

        boolean status = RSACoder.verify(data, publicKey, sign);
        System.out.println("状态：" + status);

    }

    public static void main(String[] args)
        throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
        InvalidKeySpecException, NoSuchPaddingException, SignatureException {
        RSACoderTest test = new RSACoderTest();
        test.initKey();
        test.test();
        test.testSign();
    }

}
