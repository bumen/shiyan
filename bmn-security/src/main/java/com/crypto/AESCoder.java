package com.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class AESCoder {

    public static final String KEY_ALGORITHM = "AES";

    public static final String CIPHER_ALGORIGHTM = "AES/ECB/PKCS5Padding";

    private static Key toKey(byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    public static byte[] decrypt(byte[] data, byte[] key)
        throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORIGHTM);
        cipher.init(Cipher.DECRYPT_MODE, k);

        return cipher.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, byte[] key)
        throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException,
        BadPaddingException, IllegalBlockSizeException {
        Key k = toKey(key);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORIGHTM);
        cipher.init(Cipher.ENCRYPT_MODE, k);

        return cipher.doFinal(data);
    }

    public static byte[] initKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }


}
