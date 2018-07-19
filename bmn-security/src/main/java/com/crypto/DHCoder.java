package com.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class DHCoder {

    public static final String KEY_ALGORITHM = "DH";

    public static final String SECRET_ALGORITHM = "AES";

    private static final int KEY_SIZE = 512;

    private static final String PUBLIC_KEY = "DHPublicKey";
    private static final String PRIVET_KEY = "DHPrivateKey";

    public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);

        KeyPair keyPair  = keyPairGenerator.generateKeyPair();

        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVET_KEY, privateKey);
        return keyMap;
    }

    public static Map<String, Object> initKey(byte[] key)
        throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException {

        //获取公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        DHParameterSpec dhParameterSpec = ((DHPublicKey)pubKey).getParams();


        //根据公钥生成另一个公钥私钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyFactory.getAlgorithm());
        keyPairGenerator.initialize(dhParameterSpec);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVET_KEY, privateKey);
        return keyMap;
    }


    public static byte[] encrypt(byte[] data, byte[] key)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = new SecretKeySpec(key, SECRET_ALGORITHM);
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, byte[] key)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
        IllegalBlockSizeException {
        SecretKey secretKey = new SecretKeySpec(key, SECRET_ALGORITHM);
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(data);
    }

    public static byte[] getSecretKey(byte[] publicKey, byte[] privateKey)
        throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        KeyAgreement keyAgreement = KeyAgreement.getInstance(keyFactory.getAlgorithm());
        keyAgreement.init(priKey);
        keyAgreement.doPhase(pubKey, true);

        SecretKey secretKey = keyAgreement.generateSecret(SECRET_ALGORITHM);
        return secretKey.getEncoded();
    }


    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVET_KEY);
        return key.getEncoded();
    }

    public static byte[] getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

}
