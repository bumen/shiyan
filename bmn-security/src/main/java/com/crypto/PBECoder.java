package com.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.text.AsyncBoxView.ChildLocator;

public abstract class PBECoder {

    public static final String ALGORITHM = "PBEWITHMD5andDES";

    public static final int ITERATION_COUNT = 100;

    public static byte[] initSlat() {
        SecureRandom random = new SecureRandom();
        return random.generateSeed(8);
    }

    private static Key toKey(String passwd) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec keySpec = new PBEKeySpec(passwd.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(keySpec);
    }

    public static byte[] encrypt(byte[] data, String passwd, byte[] slat)
        throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key key = toKey(passwd);

        PBEParameterSpec parameterSpec = new PBEParameterSpec(slat, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        return cipher.doFinal(data);

    }

    public static byte[] decrypt(byte[] data, String passwd, byte[] slat)
        throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key key = toKey(passwd);

        PBEParameterSpec parameterSpec = new PBEParameterSpec(slat, ITERATION_COUNT);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        return cipher.doFinal(data);
    }

}
