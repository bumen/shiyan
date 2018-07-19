package com.crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CertificateCoder {

    public static final String CERT_TYPE = "X.509";

    private static Certificate getCertificate(String keyStorePath, String alias, String passwd)
        throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        KeyStore ks = getKeyStore(keyStorePath, passwd);
        return (Certificate) ks.getCertificate(alias);
    }

    private static PrivateKey getPrivateKeyByKeyStore(String keyStorePath, String alias, String passwd)
        throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        KeyStore ks = getKeyStore(keyStorePath, passwd);
        return (PrivateKey) ks.getKey(alias, passwd.toCharArray());
    }

    public static PublicKey getPublicKeyByCertificate(String certificatePath) throws CertificateException {
        Certificate certificate = getCertificate(certificatePath);
        return certificate.getPublicKey();
    }


    private static KeyStore getKeyStore(String keyStorePath, String passwd) throws KeyStoreException {
        KeyStore ks = KeyStore.getInstance("pkcs12");
        try (FileInputStream is = new FileInputStream(keyStorePath)) {
            ks.load(is, passwd.toCharArray());
            return ks;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Certificate getCertificate(String certificatePath) throws CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
        try (FileInputStream is = new FileInputStream(certificatePath)) {
            Certificate c = certificateFactory.generateCertificate(is);
            return c;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String alias, String passwd)
        throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, passwd);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias, String passwd)
        throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnrecoverableKeyException,
        KeyStoreException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, passwd);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(byte[] data, String certificatePath)
        throws CertificateException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
        BadPaddingException, IllegalBlockSizeException {
        PublicKey publicKey = getPublicKeyByCertificate(certificatePath);

        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }


    public static byte[] decryptByPublicKey(byte[] data, String certificatePath)
        throws CertificateException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
        BadPaddingException, IllegalBlockSizeException {
        PublicKey publicKey = getPublicKeyByCertificate(certificatePath);

        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    public static byte[] sign(byte[] sign, String keyStorePath, String alias, String passwd)
        throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, InvalidKeyException, SignatureException {
        X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias, passwd);
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, passwd);

        signature.initSign(privateKey);
        signature.update(sign);
        return signature.sign();
    }

    public static boolean verify(byte[] data, byte[] sign, String certificatePath)
        throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        X509Certificate certificate = (X509Certificate) getCertificate(certificatePath);

        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        signature.initVerify(certificate);
        signature.update(data);
        return signature.verify(sign);
    }

    public static boolean verifyCertificateSign(String caCertificatePath, String certificatePath)
        throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
        InvalidKeyException, SignatureException, NoSuchProviderException {
        X509Certificate certificate = (X509Certificate) getCertificate(certificatePath);

        PublicKey publicKey1 = certificate.getPublicKey();

        byte[] sign = certificate.getSignature();

        PublicKey publicKey = getPublicKeyByCertificate(caCertificatePath);

        certificate.verify(publicKey);

        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        signature.initVerify(publicKey);

        byte[] data = publicKey1.getEncoded();

        signature.update(data);

        return signature.verify(sign);
    }
}
