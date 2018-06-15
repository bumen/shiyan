package com.bmn.gm.util;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.util.Base64;

/**
 * Created by Administrator on 2017/7/17.
 */
public class EncryptionUtil {
    public static final String ALGORITHM = "RSA";
    public static final String PADDING = "RSA/NONE/NoPadding";
    public static final String PROVIDER = "BC";
    public static final String PRIVATE_KEY_FILE = "public.key";
    public static final String PUBLIC_KEY_FILE = "private.key";


    public static void generateKey() {
        try {
            //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
            keyGen.initialize(256);
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();

            ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(key.getPublic());
            publicKeyOS.close();

            ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(key.getPrivate());
            privateKeyOS.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
           // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            final Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static String decrypt(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
           // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            final Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(dectyptedText);
    }


    public static boolean areKeysPersent() {
        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        try {


            if (!areKeysPersent()) {
                generateKey();
            }

            final String originalText = "123123123123123123";

            ObjectInputStream inputStream = null;

            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();

            System.out.println("publicKey= " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));

            final byte[] cipherText = encrypt(originalText, publicKey);
            Base64.Encoder encoder = Base64.getEncoder();
            Base64.Decoder decoder = Base64.getDecoder();

            String cipherTextBase64 = encoder.encodeToString(cipherText);

            byte[] cipherTextArray = decoder.decode(cipherTextBase64);

            inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
            final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
            final String plainText = decrypt(cipherTextArray, privateKey);

            System.out.println("Original = "+ originalText);
            System.out.println("Encrypted = " + cipherTextBase64);
            System.out.println("Decrypted = " + plainText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
