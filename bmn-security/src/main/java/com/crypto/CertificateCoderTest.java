package com.crypto;

import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CertificateCoderTest {

    private String passwd = "123123";
    private String alias = "1";

    private static String certificatePath = "E:\\ca\\certs\\server.cer";
    private String keyStorePath = "E:\\ca\\certs\\server.p12";

    private static String caKeyStorePath = "E:\\ca\\certs\\ca.p12";
    private static String caCertificatePath = "E:\\ca\\certs\\ca.cer";

    public static void main(String[] args)
        throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
        BadPaddingException, IllegalBlockSizeException, KeyStoreException, InvalidKeyException, SignatureException, NoSuchProviderException {
        CertificateCoderTest test = new CertificateCoderTest();
        test.test();

        test.test2();

        test.testSign();

        boolean t = CertificateCoder.verifyCertificateSign(caCertificatePath, certificatePath);
        System.out.println("证书验证: " + t);
    }

    public void test()
        throws CertificateException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {
        String str = "数字证书";
        byte[] data = str.getBytes();

        byte[] encrypt = CertificateCoder.encryptByPublicKey(data, certificatePath);

        byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt, keyStorePath, alias, passwd);

        System.out.println("加密前：\n" + str);
        System.out.println("解密后：\n" + new String(decrypt));
    }

    public void test2()
        throws NoSuchPaddingException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
        BadPaddingException, IllegalBlockSizeException, InvalidKeyException, CertificateException {
        String str = "数字证书";
        byte[] data = str.getBytes();

        byte[] encrypt = CertificateCoder.encryptByPrivateKey(data, keyStorePath, alias, passwd);

        byte[] decrypt = CertificateCoder.decryptByPublicKey(encrypt, certificatePath);


        System.out.println("加密前：\n" + str);
        System.out.println("解密后：\n" + new String(decrypt));
    }

    public void testSign()
        throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, InvalidKeyException, SignatureException, CertificateException {
        String str = "签名";
        byte[] data = str.getBytes();

        byte[] sign = CertificateCoder.sign(data, keyStorePath, alias, passwd);
        System.out.println("签名：\n" + Base64.getEncoder().encodeToString(sign));

        boolean status = CertificateCoder.verify(data, sign, certificatePath);
        System.out.println("状态：" + status);

    }
}
