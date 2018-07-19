package com.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DHCoderTest {
    //甲方公钥
    private byte[] publicKey1;
    //甲方私钥
    private byte[] privateKey1;
    //甲方本地私密密钥
    private byte[] key1;

    //乙方公钥
    private byte[] publicKey2;
    //乙方私钥
    private byte[] privateKey2;
    //乙方本地私密密钥
    private byte[] key2;


    public static void main(String[] args)
        throws InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
        InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        DHCoderTest coder = new DHCoderTest();
        coder.initKey();

        coder.test();
    }

    public final void initKey()
        throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        Map<String, Object> keyMap1 = DHCoder.initKey();
        publicKey1 = DHCoder.getPublicKey(keyMap1);
        privateKey1 = DHCoder.getPrivateKey(keyMap1);

        System.out.println("甲方公钥：" + Base64.getEncoder().encodeToString(publicKey1));
        System.out.println("甲方私钥：" + Base64.getEncoder().encodeToString(privateKey1));

        Map<String, Object> keyMap2 = DHCoder.initKey(publicKey1);
        publicKey2 = DHCoder.getPublicKey(keyMap2);
        privateKey2 = DHCoder.getPrivateKey(keyMap2);
        System.out.println("乙方公钥：" + Base64.getEncoder().encodeToString(publicKey2));
        System.out.println("乙方私钥：" + Base64.getEncoder().encodeToString(privateKey2));

        key1 = DHCoder.getSecretKey(publicKey2, privateKey1);
        key2 = DHCoder.getSecretKey(publicKey1, privateKey2);
        System.out.println("甲方本地密钥：\n" + Base64.getEncoder().encodeToString(key1));
        System.out.println("乙方本地密钥：\n" + Base64.getEncoder().encodeToString(key2));

        System.out.println(Arrays.equals(key1, key2));
    }

    public void test()
        throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String input1 = "DH: 密码交换算法";

        byte[] code1 = DHCoder.encrypt(input1.getBytes(), key1);

        byte[] decode1 = DHCoder.decrypt(code1, key2);

        System.out.println("解密: " + new String(decode1));
    }
}
